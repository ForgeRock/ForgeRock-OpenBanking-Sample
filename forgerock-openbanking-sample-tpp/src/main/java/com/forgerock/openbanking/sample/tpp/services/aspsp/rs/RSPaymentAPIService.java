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


import com.forgerock.openbanking.sample.tpp.model.aspsp.AspspConfiguration;
import com.forgerock.openbanking.sample.tpp.model.oidc.AccessTokenResponse;
import com.forgerock.openbanking.sample.tpp.model.payment.PaymentSetup;
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
import uk.org.openbanking.datamodel.payment.*;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetupResponse1;
import uk.org.openbanking.datamodel.payment.paymentsubmission.OBPaymentSubmission1;
import uk.org.openbanking.datamodel.payment.paymentsubmission.OBPaymentSubmissionResponse1;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * OIDC flows contains the functions needed by an OpenID client.
 */
@Service
public class RSPaymentAPIService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSPaymentAPIService.class);

    @Resource(name = "restTemplateForRS")
    private RestTemplate restTemplate;

    /**
     * Register a new payment
     *
     * @param accessTokenResponse the access token for registering the payment
     * @return the payment response from the RS
     */
    public PaymentSetup registerPayment(AspspConfiguration aspspConfiguration, AccessTokenResponse accessTokenResponse)
            throws Exception {
        String uid = UUID.randomUUID().toString();
        OBInitiation1 initiation = generateInitiation(aspspConfiguration, uid);
        OBPaymentDataSetup1 dataSetup = new OBPaymentDataSetup1()
                .initiation(initiation);

        OBPaymentSetup1 paymentSetupRequest = new OBPaymentSetup1()
                .risk(this.generateRisk())
                .data(dataSetup);


        //Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(OBHeaders.AUTHORIZATION, "Bearer " + accessTokenResponse.access_token);
        headers.add(OBHeaders.X_IDEMPOTENCY_KEY, uid);
        //It's optional and can probably be replaced by a JWS content instead.
        //headers.add(OBHeaders.X_JWS_SIGNATURE, "");
        headers.add(OBHeaders.X_FAPI_FINANCIAL_ID, aspspConfiguration.getFinancialId());
        //We don't have the user last logged time
        //headers.add(OBHeaders.X_FAPI_CUSTOMER_LAST_LOGGED_TIME, "");
        headers.add(OBHeaders.X_FAPI_CUSTOMER_IP_ADDRESS, "");
        headers.add(OBHeaders.X_FAPI_INTERACTION_ID, uid);
        headers.add(OBHeaders.ACCEPT, "application/json");

        //Send request
        HttpEntity<OBPaymentSetup1> request = new HttpEntity<>(paymentSetupRequest, headers);

        try {
            OBPaymentSetupResponse1 obPaymentSetupResponse1 = restTemplate.postForObject(
                    aspspConfiguration.getDiscoveryAPILinksPayment().getCreateSingleImmediatePayment(), request, OBPaymentSetupResponse1.class);
            PaymentSetup paymentSetup = new PaymentSetup();
            paymentSetup.setFinancialId(aspspConfiguration.getFinancialId());
            paymentSetup.setPaymentSetupRequest(paymentSetupRequest);
            paymentSetup.setPaymentSetupResponse(obPaymentSetupResponse1);
            paymentSetup.setIdempotencyKey(uid);
            paymentSetup.setInteractionId(uid);
            paymentSetup.setPaymentId(obPaymentSetupResponse1.getData().getPaymentId());

            return paymentSetup;
        } catch (HttpClientErrorException e) {
            LOGGER.error("Could not register payment to RS", e);
            throw new Exception(e.getResponseBodyAsString(), e);
        }
    }

    /**
     * Send a payment submission
     *
     * @param paymentID           the payment ID we want to submit
     * @param accessTokenResponse the access token to have the authorization of making a payment submission to the RS
     * @return the payment response
     */
    public OBPaymentSubmissionResponse1 paymentSubmission(AspspConfiguration aspspConfiguration,
                                                          PaymentSetup paymentSetup, String paymentID,
                                                          AccessTokenResponse accessTokenResponse)
            throws Exception {
        String uid = UUID.randomUUID().toString();
        LOGGER.debug("We send a payment submission for the payment ID '{}'", paymentID);

        //Request body
        OBPaymentDataSubmission1 data = new OBPaymentDataSubmission1()
                .paymentId(paymentID)
                .initiation(paymentSetup.getPaymentSetupResponse().getData().getInitiation());

        OBPaymentSubmission1 paymentSubmission = new OBPaymentSubmission1()
                .data(data)
                .risk(this.generateRisk());

        //Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(OBHeaders.AUTHORIZATION, "Bearer " + accessTokenResponse.access_token);
        headers.add(OBHeaders.X_IDEMPOTENCY_KEY, uid);
        //It's optional and can probably be replaced by a JWS content instead.
        //headers.add(OBHeaders.X_JWS_SIGNATURE, "");
        headers.add(OBHeaders.X_FAPI_FINANCIAL_ID, aspspConfiguration.getFinancialId());
        //We don't have the user last logged time
        //headers.add(OBHeaders.X_FAPI_CUSTOMER_LAST_LOGGED_TIME, "");
        headers.add(OBHeaders.X_FAPI_CUSTOMER_IP_ADDRESS, "");
        headers.add(OBHeaders.X_FAPI_INTERACTION_ID, uid);
        headers.add(OBHeaders.CONTENT_TYPE, "application/json");
        headers.add(OBHeaders.ACCEPT, "application/json");

        //Send request
        HttpEntity<OBPaymentSubmission1> request = new HttpEntity<>(paymentSubmission, headers);
        try {
            return restTemplate.postForObject(aspspConfiguration.getDiscoveryAPILinksPayment().getCreatePaymentSubmission(),
                    request, OBPaymentSubmissionResponse1.class);
        } catch (HttpClientErrorException e) {
            throw new Exception(e.getResponseBodyAsString(), e);
        }
    }

    private OBInitiation1 generateInitiation(AspspConfiguration aspspConfiguration, String uid) {
        //TODO complete those values with what ever you like
        String accountNumber = "21325698";
        String sortCode = "080800";
        String accountName = "ACME Inc";
        OBExternalAccountIdentification2Code accountIdentificationCode = OBExternalAccountIdentification2Code
                .SortCodeAccountNumber;
        return new OBInitiation1()
                .instructionIdentification(aspspConfiguration.getFinancialId())
                .endToEndIdentification("FRESCO.21302.GFX.20")
                .instructedAmount(new ActiveOrHistoricCurrencyAndAmount()
                        .amount("165.88")
                        .currency("GBP"))
                .creditorAccount(new OBCashAccountCreditor1()
                        .schemeName(accountIdentificationCode)
                        .identification( sortCode + accountNumber)
                        .name(accountName)
                )
                .remittanceInformation(new OBRemittanceInformation1()
                        .reference(
                        "FRESCO-101")
                        .unstructured("Internal ops code 5120101"));
    }

    private OBRisk1 generateRisk() {

        //TODO complete those values with what ever you like
        OBExternalPaymentContext1Code paymentContextCode = OBExternalPaymentContext1Code.EcommerceGoods;
        String merchantCategoryCode = "5967";
        String merchantCustomerIdentification = "053598653254";
        List<String> addressLines = Stream.of("Flat 7", "Acacia Lodge").collect(Collectors.toList());
        String streetName = "Acacia Avenue";
        String buildingNumber = "27";
        String postCode ="GU31 2ZZ";
        String townName = "Sparsholt";
        List<String> countySubDivision = Stream.of("Wessex").collect(Collectors.toList());
        String country = "UK";

        return new OBRisk1()
                .paymentContextCode(paymentContextCode)
                .merchantCategoryCode(merchantCategoryCode)
                .merchantCustomerIdentification(merchantCustomerIdentification)
                .deliveryAddress(new PostalAddress18()
                    .addressLine(addressLines)
                    .streetName(streetName)
                    .buildingNumber(buildingNumber)
                    .postCode(postCode)
                    .townName(townName)
                    .countrySubDivision(countySubDivision)
                    .country(country)
                );
    }
}
