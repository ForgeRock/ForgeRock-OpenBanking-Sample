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
package com.forgerock.openbanking.sample.tpp.services.aspsp.as;

import com.forgerock.openbanking.sample.tpp.model.as.registration.OIDCRegistrationResponse;
import com.forgerock.openbanking.sample.tpp.model.aspsp.AspspConfiguration;
import com.forgerock.openbanking.sample.tpp.model.claim.Claim;
import com.forgerock.openbanking.sample.tpp.model.oidc.AccessTokenResponse;
import com.forgerock.openbanking.sample.tpp.oidc.OIDCConstants;
import com.forgerock.openbanking.sample.tpp.openbanking.OpenBankingConstants;
import com.forgerock.openbanking.sample.tpp.services.JwkManagementService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * OIDC flows contains the functions needed by an OpenID client.
 */
@Service
public class AspspAsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AspspAsService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JwkManagementService jwkManagementService;

    /**
     * Exchange a code into an access token
     *
     * @param code an authorization code
     * @return an access token response
     */
    public AccessTokenResponse exchangeCode(String code, AspspConfiguration aspspConfiguration, String redirectUri)
            throws Exception {
        String clientAuthenticationJwt = generateClientAuthenticationJwt(aspspConfiguration);

        //Request body
        LOGGER.debug("We exchange the code '{}' for an access token.", code);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set(OIDCConstants.OIDCClaim.GRANT_TYPE, OIDCConstants.GrantType.AUTHORIZATION_CODE);
        params.set(OIDCConstants.OIDCClaim.CODE, code);
        params.set(OIDCConstants.OIDCClaim.REDIRECT_URI, redirectUri);

        LOGGER.debug("We authenticate to the AS via the client authentication JWT");
        /* we authenticate to the AS via the client authentication JWT.*/
        params.set(OIDCConstants.OIDCClaim.CLIENT_ASSERTION_TYPE, OIDCConstants.JWT_BEARER_CLIENT_ASSERTION_TYPE);
        params.set(OIDCConstants.OIDCClaim.CLIENT_ASSERTION, clientAuthenticationJwt);
        LOGGER.debug("Client credential JWT : '{}'",clientAuthenticationJwt);

        //Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //Send request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        LOGGER.debug("Send new authorization code request to OpenAM.");

        return restTemplate.postForObject(aspspConfiguration.getOidcDiscoveryResponse().getTokenEndpoint(),
                request, AccessTokenResponse.class);
    }

    /**
     * Get an access token via the client credential flow
     *
     * @return an access token reponse
     */
    public AccessTokenResponse clientCredential(AspspConfiguration aspspConfiguration, List<String> scopes)
            throws Exception {
        String clientAuthenticationJwt = generateClientAuthenticationJwt(aspspConfiguration);
        //Request body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.set(OIDCConstants.OIDCClaim.GRANT_TYPE, OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        params.set(OIDCConstants.OIDCClaim.SCOPE, scopes.stream().collect(Collectors.joining(" ")));

        LOGGER.debug("We authenticate to the AS via the client authentication JWT");
        params.set(OIDCConstants.OIDCClaim.CLIENT_ASSERTION_TYPE, OIDCConstants.JWT_BEARER_CLIENT_ASSERTION_TYPE);
        params.set(OIDCConstants.OIDCClaim.CLIENT_ASSERTION, clientAuthenticationJwt);
        LOGGER.debug("Client credential JWT : '{}'", clientAuthenticationJwt);

        //Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //Send request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params,
                headers);
        return restTemplate.postForObject(aspspConfiguration.getOidcDiscoveryResponse().getTokenEndpoint(),
                request, AccessTokenResponse.class);
    }

    /**
     * Generate a new request parameter. Only needed when the signature and encryption keys needs to be rotated.
     *
     * @return the request parameter JWT, containing some claims.
     * @throws JOSEException
     */
    public String generateRequestParameter(AspspConfiguration aspspConfiguration, String intentId, String state,
                                           String nonce, String redirectUri, List<String> scopes) throws Exception {
        //Span are needed for doing a Zipkin tracing.
        LOGGER.debug("Generate a request parameter for the intent ID '{}', state '{}' and nonce '{}'", intentId,
                state, nonce);
        OIDCRegistrationResponse oidcRegistrationResponse = aspspConfiguration.getOidcRegistrationResponse();

        JWTClaimsSet.Builder requestParameterClaims;
        requestParameterClaims = new JWTClaimsSet.Builder();
        requestParameterClaims.audience(aspspConfiguration.getOidcDiscoveryResponse().getIssuer());
        requestParameterClaims.expirationTime(new Date(new Date().getTime() + Duration.ofHours(1).toMillis()));
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.RESPONSE_TYPE, OIDCConstants.ResponseType.CODE + " " + OIDCConstants.ResponseType.ID_TOKEN);
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.CLIENT_ID, oidcRegistrationResponse.getClientId());
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.REDIRECT_URI, redirectUri);
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.SCOPE, scopes.stream().collect(Collectors.joining(" ")));
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.STATE, state);
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.NONCE, nonce);

        //We will ask some claims and will do a policy enforcement by using the acr essential claim.
        JSONObject claims = new JSONObject();
        JSONObject idTokenClaims = new JSONObject();
        idTokenClaims.put(OpenBankingConstants.IdTokenClaim.ACR, new Claim(true, OIDCConstants.OIDCClaim.OB_ACR_VALUE).toJson());
        idTokenClaims.put(OpenBankingConstants.IdTokenClaim.INTENT_ID,
                new Claim(true, intentId).toJson());
        claims.put(OIDCConstants.OIDCClaim.ID_TOKEN, idTokenClaims);
        JSONObject userInfoClaims = new JSONObject();
        userInfoClaims.put(OpenBankingConstants.IdTokenClaim.INTENT_ID,
                new Claim(true, intentId).toJson());
        claims.put(OIDCConstants.OIDCClaim.USER_INFO, userInfoClaims);
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.CLAIMS, claims);
        LOGGER.debug("Request parameter JWS : '{}'",
                jwkManagementService.signJwt(oidcRegistrationResponse.getClientId(), requestParameterClaims.build()));

        return jwkManagementService.signJwt(oidcRegistrationResponse.getClientId(),
                requestParameterClaims.build());
    }

    /**
     * Get an access token via the client credential flow
     *
     * @return the redirect uri
     */
    public String hybridFlow(AspspConfiguration aspspConfiguration, String state, String nonce, String requestParameter,
                             String redirectUri, List<String> scopes) {
        LOGGER.debug("Start the hybrid flow by redirecting the user to the authorize endpoint");
        //Request body
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(aspspConfiguration.getOidcDiscoveryResponse().getAuthorizationEndpoint());

        builder.queryParam(OIDCConstants.OIDCClaim.RESPONSE_TYPE, OIDCConstants.ResponseType.CODE + " " + OIDCConstants.ResponseType.ID_TOKEN);
        builder.queryParam(OIDCConstants.OIDCClaim.CLIENT_ID, aspspConfiguration.getOidcRegistrationResponse().getClientId());
        builder.queryParam(OIDCConstants.OIDCClaim.STATE, state);
        builder.queryParam(OIDCConstants.OIDCClaim.NONCE, nonce);
        builder.queryParam(OIDCConstants.OIDCClaim.SCOPE, scopes.stream().collect(Collectors.joining(" ")));
        builder.queryParam(OIDCConstants.OIDCClaim.REDIRECT_URI, redirectUri);
        builder.queryParam(OIDCConstants.OIDCClaim.REQUEST, requestParameter);

        return builder.build().encode().toUriString();
    }

    /**
     * Generate a new client authentication JWT.
     *
     * @return a JWT that can be used to authenticate Kyle to the AS.
     */
    public String generateClientAuthenticationJwt(AspspConfiguration aspspConfiguration) throws Exception {
        String clientId = aspspConfiguration.getOidcRegistrationResponse().getClientId();
        JWTClaimsSet.Builder requestParameterClaims;
        requestParameterClaims = new JWTClaimsSet.Builder();
        //By putting the issuer id as subject, this JWT will play the role of client credential. Never generate
        // another JWT with client id as subject! Otherwise this JWT can be used as credential as well!
        requestParameterClaims.subject(clientId);
        requestParameterClaims.audience(aspspConfiguration.getOidcDiscoveryResponse().getIssuer());
        requestParameterClaims.expirationTime(new Date(new Date().getTime() + Duration.ofMinutes(10).toMillis()));
        return jwkManagementService.signJwt(clientId, requestParameterClaims.build());
    }
}
