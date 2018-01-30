/*
 * The contents of this file are subject to the terms of the Common Development and
 *  Distribution License (the License). You may not use this file except in compliance with the
 *  License.
 *
 *  You can obtain a copy of the License at https://forgerock.org/license/CDDLv1.0.html. See the License for the
 *  specific language governing permission and limitations under the License.
 *
 *  When distributing Covered Software, include this CDDL Header Notice in each file and include
 *  the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 *  Header, with the fields enclosed by brackets [] replaced by your own identifying
 *  information: "Portions copyright [year] [name of copyright owner]".
 *
 *  Copyright 2018 ForgeRock AS.
 *
 */
package com.forgerock.openbanking.sample.tpp.api.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.sample.tpp.configuration.TppConfiguration;
import com.forgerock.openbanking.sample.tpp.model.as.discovery.OIDCDiscoveryResponse;
import com.forgerock.openbanking.sample.tpp.model.as.registration.OIDCRegistrationRequest;
import com.forgerock.openbanking.sample.tpp.model.as.registration.OIDCRegistrationResponse;
import com.forgerock.openbanking.sample.tpp.model.aspsp.AspspConfiguration;
import com.forgerock.openbanking.sample.tpp.oidc.OIDCConstants;
import com.forgerock.openbanking.sample.tpp.openbanking.OpenBankingConstants;
import com.forgerock.openbanking.sample.tpp.repository.AspspConfigurationMongoRepository;
import com.forgerock.openbanking.sample.tpp.services.JwkManagementService;
import com.forgerock.openbanking.sample.tpp.services.aspsp.as.ASDiscoveryService;
import com.forgerock.openbanking.sample.tpp.services.aspsp.as.ASPSPASRegistrationService;
import com.forgerock.openbanking.sample.tpp.services.aspsp.rs.RSDiscoveryService;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPI;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksAccount;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryResponse;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Controller
public class TPPRegistrationController implements TPPRegistration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TPPRegistrationController.class);

    @Autowired
    private ASDiscoveryService asDiscoveryService;
    @Autowired
    private ASPSPASRegistrationService aspspAsRegistrationService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TppConfiguration tppConfiguration;
    @Autowired
    private RSDiscoveryService rsDiscoveryService;
    @Autowired
    private JwkManagementService jwkManagementService;
    @Autowired
    private AspspConfigurationMongoRepository aspspConfigurationRepository;

    @Override
    public ResponseEntity registerToAspsp(
            @ApiParam(value = "The ASPSP name", required = true)
            @RequestParam("name") String name,

            @ApiParam(value = "The ASPSP logo", required = false)
            @RequestParam(value = "logo", required = false) String logo,

            @ApiParam(value = "The ASPSP financial ID", required = true)
            @RequestParam(value = "financialID", required = true) String financialID,

            @ApiParam(value = "The ASPSP-AS OIDC root endpoint", required = true)
            @RequestParam("oidcRootEndpoint") String oidcRootEndpoint,

            @ApiParam(value = "The ASPSP-RS discovery endpoint", required = true)
            @RequestParam("discoveryEndpoint") String discoveryEndpoint,

            @ApiParam(value = "The ASPSP-AS register endpoint", required = false)
            @RequestParam(value = "registrationEndpoint", required = false) String registrationEndpoint
    ) {
        LOGGER.debug("Received a aspsp request for an ASPSP: name '{}', logo '{}', financialID {}," +
                " oidcRootEndpoint {}, discoveryEndpoint {}, registrationEndpoint {}", name, logo, financialID,
                oidcRootEndpoint, discoveryEndpoint, registrationEndpoint
        );

        LOGGER.debug("Call the OIDC discovery endpoint {}", oidcRootEndpoint);
        OIDCDiscoveryResponse oidcDiscoveryResponse = asDiscoveryService.discovery(oidcRootEndpoint);
        if (registrationEndpoint == null) {
            registrationEndpoint = oidcDiscoveryResponse.getRegistrationEndpoint();
        }
        LOGGER.debug("The OIDC aspsp endpoint: {}", oidcRootEndpoint);

        try {
            String ssa = tppConfiguration.getSsa();
            LOGGER.debug("The SSA we are going to use: {}", ssa);

            String registrationRequest = generateRegistrationRequest(oidcDiscoveryResponse, generateOIDCRegistrationRequest(oidcDiscoveryResponse), ssa);
            LOGGER.debug("The aspsp request generated : {}", registrationRequest);

            OIDCRegistrationResponse oidcRegistrationResponse = aspspAsRegistrationService.register(
                    registrationEndpoint, registrationRequest);
            LOGGER.debug("We are successfully registered : {}", oidcRegistrationResponse);

            LOGGER.debug("We call the RS discovery endpoint : {}", discoveryEndpoint);
            OBDiscoveryResponse rsDiscovery = rsDiscoveryService.discovery(discoveryEndpoint);
            LOGGER.debug("The RS discovery response : {}", rsDiscovery);

            Optional<OBDiscoveryAPI<OBDiscoveryAPILinksPayment>> paymentInitiationAPI = rsDiscovery.getData().getPaymentInitiationAPI(tppConfiguration.getVersion());
            Optional<OBDiscoveryAPI<OBDiscoveryAPILinksAccount>> accountAndTransactionAPI = rsDiscovery.getData().getAccountAndTransactionAPI(tppConfiguration.getVersion());

            if (!paymentInitiationAPI.isPresent()
                    || !accountAndTransactionAPI.isPresent()) {
                LOGGER.warn("RS doesn't implement version '{}' of the Open Banking standard.", tppConfiguration.getVersion());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        "RS doesn't implement version '" + tppConfiguration.getVersion()
                                + "' of the Open Banking standard and this TPP only support this version.");
            }

            LOGGER.debug("Register the ASPSP configuration");
            AspspConfiguration aspspConfiguration = new AspspConfiguration();
            aspspConfiguration.setDiscoveryEndpoint(discoveryEndpoint);
            aspspConfiguration.setOidcDiscoveryResponse(oidcDiscoveryResponse);
            aspspConfiguration.setName(name);
            aspspConfiguration.setLogo(logo);
            aspspConfiguration.setFinancialId(financialID);
            aspspConfiguration.setSsa(ssa);
            aspspConfiguration.setOidcRegistrationResponse(oidcRegistrationResponse);
            aspspConfiguration.setRegistrationEndpoint(registrationEndpoint);
            aspspConfiguration.setDiscoveryAPILinksPayment(paymentInitiationAPI.get().getLinks());
            aspspConfiguration.setDiscoveryAPILinksAccount(accountAndTransactionAPI.get().getLinks());

            return ResponseEntity.ok(aspspConfigurationRepository.save(aspspConfiguration));
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("Couldn't read the error returned by the RS: {}", e.getResponseBodyAsString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            LOGGER.error("An error happened", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity unregisterToAspsp(
            @ApiParam(value = "The ASPSP ID", required = true)
            @PathVariable("aspspId") String aspspId
    ){
        Optional<AspspConfiguration> optionalAspspConfiguration = aspspConfigurationRepository.findById(aspspId);
        if (!optionalAspspConfiguration.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ASPSP '" + aspspId + "' wasn't registered.");
        }
        aspspAsRegistrationService.unregister(optionalAspspConfiguration.get());
        return ResponseEntity.ok("ASPSP '" + aspspId + "'  unregistered successfully");
    }

    private OIDCRegistrationRequest generateOIDCRegistrationRequest(OIDCDiscoveryResponse oidcDiscoveryResponse) {

        //TODO verify that the OIDC provider supports the features we are asking for
        OIDCRegistrationRequest oidcRegistrationRequest = new OIDCRegistrationRequest();

        oidcRegistrationRequest.setScopes(
                Arrays.asList(OpenBankingConstants.Scope.OPENID,
                        OpenBankingConstants.Scope.ACCOUNTS,
                        OpenBankingConstants.Scope.PAYMENTS));
        oidcRegistrationRequest.setRedirectUris(Arrays.asList(tppConfiguration.getAispRedirectUri(), tppConfiguration.getPispRedirectUri()));

        oidcRegistrationRequest.setGrantTypes(Arrays.asList(OIDCConstants.GrantType.AUTHORIZATION_CODE,
                OIDCConstants.GrantType.REFRESH_TOKEN,
                OIDCConstants.GrantType.CLIENT_CREDENTIAL));

        oidcRegistrationRequest.setResponseTypes(Arrays.asList(OIDCConstants.ResponseType.CODE,
                OIDCConstants.ResponseType.TOKEN,
                OIDCConstants.ResponseType.CODE + " " + OIDCConstants.ResponseType.ID_TOKEN));
        oidcRegistrationRequest.setApplicationType(OpenBankingConstants.RegistrationTppRequestClaims.APPLICATION_TYPE_WEB);

        oidcRegistrationRequest.setRedirectUris(Arrays.asList(tppConfiguration.getPispRedirectUri(),
                tppConfiguration.getAispRedirectUri()));

        oidcRegistrationRequest.setTokenEndpointAuthMethod(OIDCConstants.TokenEndpointAuthMethods.PRIVATE_KEY_JWT);
        oidcRegistrationRequest.setTokenEndpointAuthSigningAlg(JWSAlgorithm.RS256.getName());
        oidcRegistrationRequest.setIdTokenSignedResponseAlg(JWSAlgorithm.ES256.getName());
        //TODO until MIT has not fixed the encryption issue, we can't use encryption in OB
        //oidcRegistrationRequest.setIdTokenEncryptedResponseAlg(JWEAlgorithm.RSA_OAEP_256.getName());
        oidcRegistrationRequest.setSubjectType(OIDCConstants.SubjectType.PUBLIC);
        oidcRegistrationRequest.setRequestObjectSigningAlg(JWSAlgorithm.RS256.getName());
        oidcRegistrationRequest.setRequestObjectEncryptionAlg(JWEAlgorithm.RSA_OAEP_256.getName());
        oidcRegistrationRequest.setRequestObjectEncryptionEnc(EncryptionMethod.A128CBC_HS256.getName());
        return oidcRegistrationRequest;
    }

    /**
     * Generate aspsp request
     *
     * @return a JWT that can be used to register the TPP
     */
    private String generateRegistrationRequest(OIDCDiscoveryResponse oidcDiscoveryResponse,
                                              OIDCRegistrationRequest oidcRegistrationRequest, String ssa) throws Exception {

        String asIssuerId = oidcDiscoveryResponse.getIssuer();
        JWTClaimsSet.Builder requestParameterClaims;
        requestParameterClaims = new JWTClaimsSet.Builder();
        requestParameterClaims.audience(asIssuerId);
        requestParameterClaims.expirationTime(new Date(new Date().getTime() + Duration.ofDays(7).toMillis()));
        Map<String, Object> requestAsClaims = objectMapper.convertValue(oidcRegistrationRequest, Map.class);
        for(Map.Entry<String, Object> entry : requestAsClaims.entrySet()) {
            requestParameterClaims.claim(entry.getKey(), entry.getValue());
        }
        requestParameterClaims.claim(OpenBankingConstants.RegistrationTppRequestClaims.SOFTWARE_STATEMENT, ssa);
        return jwkManagementService.signJwt(tppConfiguration.getSoftwareId(), requestParameterClaims.build());
    }
}
