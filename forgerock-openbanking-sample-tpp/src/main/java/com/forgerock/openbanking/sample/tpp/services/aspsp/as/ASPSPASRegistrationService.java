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
import com.forgerock.openbanking.sample.tpp.repository.AspspConfigurationMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class ASPSPASRegistrationService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ASPSPASRegistrationService.class);
    @Resource(name = "restTemplateForRS")
    private RestTemplate restTemplate;
    @Autowired
    private AspspConfigurationMongoRepository aspspConfigurationRepository;

    public OIDCRegistrationResponse register(String registrationEndpoint, String registrationRequest) throws HttpClientErrorException {
        LOGGER.info("Register a new TPP to the ASPSP-AS");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/jwt"));
        //Send request
        HttpEntity<String> request = new HttpEntity<>(registrationRequest, headers);
        LOGGER.debug("Send aspsp request to the ASPSP-AS {}", registrationEndpoint);
        return restTemplate.postForObject(registrationEndpoint, request, OIDCRegistrationResponse.class);
    }

    public void unregister(AspspConfiguration aspspConfiguration) throws HttpClientErrorException {
        LOGGER.info("Unregister the TPP to the ASPSP-AS");
        restTemplate.delete(aspspConfiguration.getRegistrationEndpoint());
        aspspConfigurationRepository.delete(aspspConfiguration);
    }
}
