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
package com.forgerock.openbanking.sample.tpp.initialise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.sample.tpp.model.aspsp.AspspConfiguration;
import com.forgerock.openbanking.sample.tpp.repository.AspspConfigurationMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
public class InitialisePreRegisteredTPP {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitialisePreRegisteredTPP.class);

    @Value("${preregisteredTpp}")
    private Resource aspspConfigurationBackup;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AspspConfigurationMongoRepository aspspConfigurationMongoRepository;

    @Bean
    public CommandLineRunner initialiseAspspConfiguration() {
        return (args) -> {
            aspspConfigurationMongoRepository.deleteAll();
            LOGGER.debug("Load ASPSP configuration from backup {}", aspspConfigurationBackup.getURI());
            AspspConfiguration preRegisteredAspspConfiguration = getPreRegisteredAspspConfiguration();
            if (preRegisteredAspspConfiguration != null && preRegisteredAspspConfiguration.getId() != null) {
                aspspConfigurationMongoRepository.save(preRegisteredAspspConfiguration);
                LOGGER.debug("ASPSP configuration loaded with success: {}", preRegisteredAspspConfiguration);
            } else {
                LOGGER.debug("No ASPSP configuration backup found.");
            }
        };
    }

    private AspspConfiguration getPreRegisteredAspspConfiguration() {
        StringBuilder result = new StringBuilder("");
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = aspspConfigurationBackup.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            return objectMapper.readValue(result.toString(), AspspConfiguration.class);
        } catch (IOException e) {
            LOGGER.error("Can't read aspspConfigurationBackup back up resource", e);
            throw new RuntimeException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("Can't close inputStream correctly", e);
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOGGER.error("Can't close BufferedReader correctly", e);
                }
            }
        }
    }
}
