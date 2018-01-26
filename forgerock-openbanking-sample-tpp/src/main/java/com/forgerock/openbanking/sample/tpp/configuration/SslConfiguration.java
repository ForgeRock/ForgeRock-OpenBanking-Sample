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
package com.forgerock.openbanking.sample.tpp.configuration;

import com.forgerock.openbanking.sample.tpp.exceptions.SslConfigurationFailure;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
public class SslConfiguration {

    private static final String JAVA_KEYSTORE = "PKCS12";

    @Value("${server.ssl.key-store}")
    private Resource keyStore;
    @Value("${server.ssl.key-store-password}")
    private String keyStorePassword;
    @Value("${server.ssl.ob-trust-store}")
    private Resource trustStore;
    @Value("${server.ssl.ob-trust-store-password}")
    private String trustStorePassword;
    @Value("${server.ssl.key-password}")
    private String keyPassword;
    @Value("${tpp.certificate.transport.alias}")
    private String keyAlias;

    @Bean
    public RestTemplate restTemplate() throws Exception {
        return createRestTemplate(false);
    }

    @Bean(name="restTemplateForRS")
    public RestTemplate restTemplateForRS() throws Exception {
        return createRestTemplate(true);
    }

    public RestTemplate createRestTemplate(boolean useLocalTruststore) throws Exception {
        try {
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder()
                    .loadKeyMaterial(
                            getStore(keyStore.getURL(), keyStorePassword.toCharArray()),
                            keyPassword.toCharArray(),
                            (aliases, socket) -> keyAlias
                    );

            if (useLocalTruststore) {
                sslContextBuilder.loadTrustMaterial(
                        getStore(trustStore.getURL(), trustStorePassword.toCharArray()),
                        null);
            }
            SSLContext sslContext = sslContextBuilder.build();
            SSLConnectionSocketFactory socketFactory;
            socketFactory = new SSLConnectionSocketFactory(sslContext);
            HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
            return new RestTemplate(factory);
        } catch (Exception e) {
            throw new SslConfigurationFailure(e);
        }
    }

    protected KeyStore getStore(final URL url, final char[] password) throws
            KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        final KeyStore store = KeyStore.getInstance(JAVA_KEYSTORE);
        InputStream inputStream = url.openStream();
        try {
            store.load(inputStream, password);
        } finally {
            inputStream.close();
        }

        return store;
    }
}
