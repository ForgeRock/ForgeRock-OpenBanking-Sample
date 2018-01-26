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


import com.forgerock.openbanking.sample.tpp.model.as.discovery.OIDCDiscoveryResponse;
import com.forgerock.openbanking.sample.tpp.oidc.OIDCConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class ASDiscoveryService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ASDiscoveryService.class);
    @Autowired
    private RestTemplate restTemplate;

    public OIDCDiscoveryResponse discovery(String oidcRootEndpoint) {
        String wellKnownEndpoint = oidcRootEndpoint + OIDCConstants.Endpoint.WELL_KNOWN;
        LOGGER.debug("Call the well-known endpoint of the AS {}", wellKnownEndpoint);

        ParameterizedTypeReference<OIDCDiscoveryResponse> ptr = new ParameterizedTypeReference<OIDCDiscoveryResponse>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(wellKnownEndpoint);
        URI uri = builder.build().encode().toUri();

        ResponseEntity<OIDCDiscoveryResponse> entity = restTemplate.exchange(
                uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }
}
