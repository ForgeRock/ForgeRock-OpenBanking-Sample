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
package com.forgerock.openbanking.sample.tpp.openbanking;

/**
 * All the constants defined by the Open Banking standard.
 */
public class OpenBankingConstants {

    public static final String BOOKED_TIME_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static class AMAccessTokenClaim {
        public static final String CLAIMS = "claims";
        public static final String ID_TOKEN = "id_token";
        public static final String INTENT_ID = "openbanking_intent_id";
    }

    public static class IdTokenClaim {
        public static final String INTENT_ID = "openbanking_intent_id";
        public static final String ACR = "acr";
        public static final String C_HASH = "c_hash";
        public static final String S_HASH = "s_hash";
        public static final String ID_TOKEN = "id_token";
        public static final String USER_INFO = "user_info";
        public static final String CLAIMS = "claims";
    }

    public static class Scope {
        public static final String PAYMENTS = "payments";
        public static final String OPENID = "openid";
        public static final String ACCOUNTS = "accounts";
    }

    public static class ParametersFieldName {
        public static final String FROM_BOOKING_DATE_TIME = "fromBookingDateTime";
        public static final String TO_BOOKING_DATE_TIME = "toBookingDateTime";
    }

    public static class RegistrationTppRequestClaims {
        public static final String SOFTWARE_ID = "software_id";
        public static final String SOFTWARE_STATEMENT = "software_statement";
        public static final String JWKS_URI = "jwks_uri";
        public static final String REDIRECT_URIS = "redirect_uris";
        public static final String APPLICATION_TYPE_WEB = "web";
    }

    public static class AMRegistrationResponseClaims {
        public static final String CLIENT_ID = "client_id";
    }

    public static class SSAClaims {
        public static final String SOFTWARE_ID = "software_id";
        public static final String SOFTWARE_CLIENT_ID = "software_client_id";
        public static final String SOFTWARE_CLIENT_DESCRIPTION = "software_client_description";
        public static final String SOFTWARE_CLIENT_NAME = "software_client_name";
        public static final String SOFTWARE_VERSION = "software_version";
        public static final String SOFTWARE_ENVIRONMENT = "software_environment";
        public static final String SOFTWARE_JWKS_ENDPOINT = "software_jwks_endpoint";
        public static final String SOFTWARE_JWKS_REVOKED_ENDPOINT = "software_jwks_revoked_endpoint";
        public static final String SOFTWARE_LOGO_URI = "software_logo_uri";
        public static final String SOFTWARE_MODE = "software_mode";
        public static final String SOFTWARE_ON_BEHALF_OF_ORG = "software_on_behalf_of_org";
        public static final String SOFTWARE_ON_BEHALF_OF_ORG_TYPE = "software_on_behalf_of_org_type";
        public static final String SOFTWARE_POLICY_URI = "software_policy_uri";
        public static final String SOFTWARE_REDIRECT_URIS = "software_redirect_uris";
        public static final String SOFTWARE_ROLES = "software_roles";
        public static final String SOFTWARE_TOS_URI = "software_tos_uri";
        public static final String ORGANISATION_COMPETENT_AUTHORITY_CLAIMS = "organisation_competent_authority_claims";
        public static final String ORG_STATUS = "org_status";
        public static final String ORG_ID = "org_id";
        public static final String ORG_NAME = "org_name";
        public static final String ORG_CONTACTS = "org_contacts";
        public static final String ORG_JWKS_ENDPOINT = "org_jwks_endpoint";
        public static final String ORG_JWKS_REVOKED_ENDPOINT = "org_jwks_revoked_endpoint";
        public static final String OB_REGISTRY_TOS = "ob_registry_tos";
    }

    public static class AISPContextClaims {
        public static final String ASPSP_SESSION_CONTEXT = "aspspSessionContext";
        public static final String ASPSP_ID = "aspspId";
    }
}
