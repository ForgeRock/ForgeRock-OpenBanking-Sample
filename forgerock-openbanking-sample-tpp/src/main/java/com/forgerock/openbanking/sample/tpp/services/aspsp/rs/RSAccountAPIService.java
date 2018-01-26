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
package com.forgerock.openbanking.sample.tpp.services.aspsp.rs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.sample.tpp.model.aspsp.AspspConfiguration;
import com.forgerock.openbanking.sample.tpp.model.oidc.AccessTokenResponse;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadData1;
import uk.org.openbanking.datamodel.account.OBReadRequest1;
import uk.org.openbanking.datamodel.account.OBReadResponse1;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * OIDC flows contains the functions needed by an OpenID client.
 */
@Service
public class RSAccountAPIService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAccountAPIService.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Resource(name = "restTemplateForRS")
    private RestTemplate restTemplate;

    /**
     * Register a new payment
     *
     * @param accessTokenResponse the access token for registering the payment
     * @return the payment response from the RS
     */
    public OBReadResponse1 createAccountRequest(AspspConfiguration aspspConfiguration, AccessTokenResponse accessTokenResponse)
            throws Exception {
        LOGGER.debug("Create an account request");
        OBReadData1 dataSetup = new OBReadData1()
                .addPermissionsItem(OBExternalPermissions1Code.ReadAccountsDetail)
                .addPermissionsItem(OBExternalPermissions1Code.ReadBalances)
                .addPermissionsItem(OBExternalPermissions1Code.ReadBeneficiariesDetail)
                .addPermissionsItem(OBExternalPermissions1Code.ReadDirectDebits)
                .addPermissionsItem(OBExternalPermissions1Code.ReadProducts)
                .addPermissionsItem(OBExternalPermissions1Code.ReadStandingOrdersDetail)
                .addPermissionsItem(OBExternalPermissions1Code.ReadTransactionsCredits)
                .addPermissionsItem(OBExternalPermissions1Code.ReadTransactionsDebits)
                .addPermissionsItem(OBExternalPermissions1Code.ReadTransactionsDetail)
                .expirationDateTime(DateTime.now().withDurationAdded(Duration.standardDays(1).getMillis(), 1))
                .transactionFromDateTime(DateTime.now().withDurationAdded(Duration.standardDays(200).getMillis(), -1))
                .transactionToDateTime(DateTime.now());

        OBReadRequest1 accountRequest = new OBReadRequest1()
                .data(dataSetup);

        String uid = UUID.randomUUID().toString();
        //Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(OBHeaders.AUTHORIZATION, "Bearer " + accessTokenResponse.access_token);
        //It's optional and can probably be replaced by a JWS content instead.
        //headers.add(OBHeaders.X_JWS_SIGNATURE, "");
        headers.add(OBHeaders.X_FAPI_FINANCIAL_ID, aspspConfiguration.getFinancialId());
        //We don't have the user last logged time
        //headers.add(OBHeaders.X_FAPI_CUSTOMER_LAST_LOGGED_TIME, "");
        headers.add(OBHeaders.X_FAPI_CUSTOMER_IP_ADDRESS, "");
        headers.add(OBHeaders.X_FAPI_INTERACTION_ID, uid);
        headers.add(OBHeaders.ACCEPT, "application/json");

        //Send request
        HttpEntity<OBReadRequest1> request = new HttpEntity<>(accountRequest, headers);

        if (LOGGER.isDebugEnabled()) {
            try {
                LOGGER.debug("Account request: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString
                        (request));
            } catch (JsonProcessingException e) {
                LOGGER.error("Could not print request", e);
            }
        }
        try {
            return restTemplate.postForObject(aspspConfiguration.getDiscoveryAPILinksAccount().getCreateAccountRequest(), request, OBReadResponse1.class);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Could not register payment to RS", e);
            throw new Exception(e.getResponseBodyAsString(), e);
        }
    }
}
