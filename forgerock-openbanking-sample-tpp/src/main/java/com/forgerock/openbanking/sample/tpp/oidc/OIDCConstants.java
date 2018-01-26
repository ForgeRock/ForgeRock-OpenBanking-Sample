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
package com.forgerock.openbanking.sample.tpp.oidc;

/**
 * All the constants defined by the OIDC standard.
 */
public class OIDCConstants {
    public static final String JWT_BEARER_CLIENT_ASSERTION_TYPE =
            "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";

    public static class Endpoint {
        public static final String WELL_KNOWN = ".well-known/openid-configuration";
    }

    public static class SubjectType {
        public static final String PUBLIC = "public";
        public static final String PAIRWISE = "pairwise";
    }

    public static class TokenEndpointAuthMethods {
        public static final String CLIENT_SECRET_POST = "client_secret_post";
        public static final String CLIENT_SECRET_BASIC = "client_secret_basic";
        public static final String CLIENT_SECRET_JWT = "client_secret_jwt";
        public static final String PRIVATE_KEY_JWT = "private_key_jwt";
    }

    public static class ResponseType {
        public static final String CODE = "code";
        public static final String ID_TOKEN = "id_token";
        public static final String TOKEN = "token";
    }

    public static class GrantType {
        public static final String CLIENT_CREDENTIAL = "client_credentials";
        public static final String AUTHORIZATION_CODE = "authorization_code";
        public static final String REFRESH_TOKEN = "refresh_token";
    }

    public static class OIDCClaim {
        public static final String GRANT_TYPE = "grant_type";
        public static final String ID_TOKEN = "id_token";
        public static final String USER_INFO = "userinfo";
        public static final String CLAIMS = "claims";
        public static final String CONSENT_APPROVAL_REDIRECT_URI = "consentApprovalRedirectUri";
        public static final String RESPONSE_TYPE = "response_type";
        public static final String CLIENT_ID = "client_id";
        public static final String REDIRECT_URI = "redirect_uri";
        public static final String REQUEST = "request";
        public static final String SCOPE = "scope";
        public static final String STATE = "state";
        public static final String NONCE = "nonce";
        public static final String CLIENT_ASSERTION_TYPE = "client_assertion_type";
        public static final String CLIENT_ASSERTION = "client_assertion";
        public static final String CODE = "code";
        public static final String OB_ACR_VALUE = "urn:openbanking:psd2:sca";
    }

}
