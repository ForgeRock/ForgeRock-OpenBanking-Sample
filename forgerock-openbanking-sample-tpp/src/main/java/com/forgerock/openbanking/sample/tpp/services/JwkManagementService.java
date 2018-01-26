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
package com.forgerock.openbanking.sample.tpp.services;

import com.forgerock.openbanking.sample.tpp.configuration.TppConfiguration;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.UUID;

@Service
public class JwkManagementService {
    private final static Logger LOGGER = LoggerFactory.getLogger(JwkManagementService.class);

    @Value("${server.ssl.key-store-password}")
    private String keyStorePassword;
    @Value("${server.ssl.key-store}")
    private Resource keyStoreResource;

    @Autowired
    private TppConfiguration tppConfiguration;

    public String signJwt(String issuerId, JWTClaimsSet jwtClaimsSet) throws Exception {
        JWK signingJwk = JWK.load(getKeyStore(), tppConfiguration.getTppSignatureCertificateAlias(), tppConfiguration.getTppSignatureCertificateAlgorithm().toCharArray());
        jwtClaimsSet = new JWTClaimsSet.Builder(jwtClaimsSet)
                .issuer(issuerId)
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .build();

        JWSHeader jwsHeader = new JWSHeader
                .Builder(JWSAlgorithm.parse(tppConfiguration.getTppSignatureCertificateAlgorithm()))
                .keyID(tppConfiguration.getTppSignatureCertificateKid())
                .build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            if (signingJwk instanceof ECKey) {
                signedJWT.sign(new ECDSASigner((ECKey) signingJwk));
            } else if (signingJwk instanceof RSAKey) {
                signedJWT.sign(new RSASSASigner((RSAKey) signingJwk));
            } else {
                throw new RuntimeException("Unknown algorithm '" + signingJwk.getClass()
                        + "' used for generate the key '" + signingJwk.getKeyID() + "'");
            }
            return signedJWT.serialize();
        } catch (JOSEException e) {
            LOGGER.error("Couldn't load the key behind the kid '{}'", signingJwk.getKeyID(), e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Get the JWK keystore
     * @return the keystore
     */
    public KeyStore getKeyStore() {
        try {
            KeyStore outStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
            outStore.load(keyStoreResource.getURL().openStream(), keyStorePassword.toCharArray());
            return outStore;
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            LOGGER.error("Couldn't read keystore", e);
            throw new RuntimeException(e);
        }
    }
}
