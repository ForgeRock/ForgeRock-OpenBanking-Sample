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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryResponse;

import javax.annotation.Resource;
import java.net.URI;

@Service
public class RSDiscoveryService {
    private final static Logger LOGGER = LoggerFactory.getLogger(RSDiscoveryService.class);
    @Resource(name = "restTemplateForRS")
    private RestTemplate restTemplate;

    public OBDiscoveryResponse discovery(String rsDiscoveryEndpoint) {
        LOGGER.debug("Call the discovery of the RS {}", rsDiscoveryEndpoint);

        ParameterizedTypeReference<OBDiscoveryResponse> ptr = new ParameterizedTypeReference<OBDiscoveryResponse>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsDiscoveryEndpoint);
        URI uri = builder.build().encode().toUri();

        ResponseEntity<OBDiscoveryResponse> entity = restTemplate.exchange(
                uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }
}
