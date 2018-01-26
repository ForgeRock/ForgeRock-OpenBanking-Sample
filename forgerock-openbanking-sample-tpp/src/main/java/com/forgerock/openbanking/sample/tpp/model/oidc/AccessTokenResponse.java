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
package com.forgerock.openbanking.sample.tpp.model.oidc;

import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

/**
 * Models a response from the OAuth 2.0 token endpoint. See RFC 6749 § 5.1.
 */
public class AccessTokenResponse {
    /**
     * The access token issued by the authorization server.
     */
    public String access_token;

    /**
     * The lifetime of the access token.
     */
    public long expires_in;

    /**
     * The openId token issued by the authorization server.
     */
    public String id_token;

    public String token_type;

    public String scope;

    public AccessTokenResponse() {
    }

    /**
     * Constructs a new response.
     *
     * @param accessToken The access token issued by the authorization server.
     * @param expiresIn   The lifetime of the access token.
     * @param openIdToken The openId token issued by the authorization server.
     * @param token_type
     * @param scope
     */
    public AccessTokenResponse(String accessToken, long expiresIn,
            String openIdToken, String token_type, String scope) {
        this.access_token = accessToken;
        this.expires_in = expiresIn;
        this.id_token = openIdToken;
        this.token_type = token_type;
        this.scope = scope;
    }

    public SignedJWT getAccessTokenJWT() throws ParseException {
        return (SignedJWT) JWTParser.parse(access_token);
    }

    public EncryptedJWT getIdToken() throws ParseException {
        return (EncryptedJWT) JWTParser.parse(id_token);
    }

    public String getToken_type() {
        return token_type;
    }

    public String getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return "AccessTokenResponse{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", id_token='" + id_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
