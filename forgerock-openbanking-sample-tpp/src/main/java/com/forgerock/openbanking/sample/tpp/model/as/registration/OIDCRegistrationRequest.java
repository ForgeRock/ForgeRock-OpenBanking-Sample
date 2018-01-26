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
package com.forgerock.openbanking.sample.tpp.model.as.registration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbusds.jose.jwk.JWK;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OIDCRegistrationRequest {

    @JsonProperty("scopes")
    private List<String> scopes;
    @JsonProperty("redirect_uris")
    private List<String> redirectUris;
    @JsonProperty("response_types")
    private List<String> responseTypes;
    @JsonProperty("grant_types")
    private List<String> grantTypes;
    @JsonProperty("application_type")
    private String applicationType;
    @JsonProperty("contacts")
    private List<String> contacts;
    @JsonProperty("client_name")
    private String clientName;
    @JsonProperty("logo_uri")
    private String logoUri;
    @JsonProperty("client_uri")
    private String clientUri;
    @JsonProperty("policy_uri")
    private String policyUri;
    @JsonProperty("tos_uri")
    private String tosUri;
    @JsonProperty("jwks_uri")
    private String jwks_uri;
    @JsonProperty("jwks")
    private List<JWK> jwks;
    @JsonProperty("sector_identifier_uri")
    private String sectorIdentifierUri;
    @JsonProperty("subject_type")
    private String subjectType;

    @JsonProperty("id_token_signed_response_alg")
    private String idTokenSignedResponseAlg;
    @JsonProperty("id_token_encrypted_response_alg")
    private String idTokenEncryptedResponseAlg;
    @JsonProperty("id_token_encrypted_response_enc")
    private String idTokenEncryptedResponseEnc;

    @JsonProperty("userinfo_signed_response_alg")
    private String userinfoSignedResponseAlg;
    @JsonProperty("userinfo_encrypted_response_alg")
    private String userinfoEncryptedResponseAlg;
    @JsonProperty("userinfo_encrypted_response_enc")
    private String userinfoEncryptedResponseEnc;

    @JsonProperty("request_object_signing_alg")
    private String requestObjectSigningAlg;
    @JsonProperty("request_object_encryption_alg")
    private String requestObjectEncryptionAlg;
    @JsonProperty("request_object_encryption_enc")
    private String requestObjectEncryptionEnc;

    @JsonProperty("token_endpoint_auth_method")
    private String tokenEndpointAuthMethod;
    @JsonProperty("token_endpoint_auth_signing_alg")
    private String tokenEndpointAuthSigningAlg;

    @JsonProperty("default_max_age")
    private String defaultMaxAge;
    @JsonProperty("require_auth_time")
    private String requireAuthTime;
    @JsonProperty("default_acr_values")
    private String defaultAcrValues;
    @JsonProperty("initiate_login_uri")
    private String initiateLoginUri;
    @JsonProperty("request_uris")
    private List<String> requestUris;

    @JsonProperty("software_statement")
    private String softwareStatement;
    @JsonProperty("software_id")
    private String softwareId;

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public List<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public List<String> getResponseTypes() {
        return responseTypes;
    }

    public void setResponseTypes(List<String> responseTypes) {
        this.responseTypes = responseTypes;
    }

    public List<String> getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(List<String> grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getClientUri() {
        return clientUri;
    }

    public void setClientUri(String clientUri) {
        this.clientUri = clientUri;
    }

    public String getPolicyUri() {
        return policyUri;
    }

    public void setPolicyUri(String policyUri) {
        this.policyUri = policyUri;
    }

    public String getTosUri() {
        return tosUri;
    }

    public void setTosUri(String tosUri) {
        this.tosUri = tosUri;
    }

    public String getJwks_uri() {
        return jwks_uri;
    }

    public void setJwks_uri(String jwks_uri) {
        this.jwks_uri = jwks_uri;
    }

    public List<JWK> getJwks() {
        return jwks;
    }

    public void setJwks(List<JWK> jwks) {
        this.jwks = jwks;
    }

    public String getSectorIdentifierUri() {
        return sectorIdentifierUri;
    }

    public void setSectorIdentifierUri(String sectorIdentifierUri) {
        this.sectorIdentifierUri = sectorIdentifierUri;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getIdTokenSignedResponseAlg() {
        return idTokenSignedResponseAlg;
    }

    public void setIdTokenSignedResponseAlg(String idTokenSignedResponseAlg) {
        this.idTokenSignedResponseAlg = idTokenSignedResponseAlg;
    }

    public String getIdTokenEncryptedResponseAlg() {
        return idTokenEncryptedResponseAlg;
    }

    public void setIdTokenEncryptedResponseAlg(String idTokenEncryptedResponseAlg) {
        this.idTokenEncryptedResponseAlg = idTokenEncryptedResponseAlg;
    }

    public String getIdTokenEncryptedResponseEnc() {
        return idTokenEncryptedResponseEnc;
    }

    public void setIdTokenEncryptedResponseEnc(String idTokenEncryptedResponseEnc) {
        this.idTokenEncryptedResponseEnc = idTokenEncryptedResponseEnc;
    }

    public String getUserinfoSignedResponseAlg() {
        return userinfoSignedResponseAlg;
    }

    public void setUserinfoSignedResponseAlg(String userinfoSignedResponseAlg) {
        this.userinfoSignedResponseAlg = userinfoSignedResponseAlg;
    }

    public String getUserinfoEncryptedResponseAlg() {
        return userinfoEncryptedResponseAlg;
    }

    public void setUserinfoEncryptedResponseAlg(String userinfoEncryptedResponseAlg) {
        this.userinfoEncryptedResponseAlg = userinfoEncryptedResponseAlg;
    }

    public String getUserinfoEncryptedResponseEnc() {
        return userinfoEncryptedResponseEnc;
    }

    public void setUserinfoEncryptedResponseEnc(String userinfoEncryptedResponseEnc) {
        this.userinfoEncryptedResponseEnc = userinfoEncryptedResponseEnc;
    }

    public String getRequestObjectSigningAlg() {
        return requestObjectSigningAlg;
    }

    public void setRequestObjectSigningAlg(String requestObjectSigningAlg) {
        this.requestObjectSigningAlg = requestObjectSigningAlg;
    }

    public String getRequestObjectEncryptionAlg() {
        return requestObjectEncryptionAlg;
    }

    public void setRequestObjectEncryptionAlg(String requestObjectEncryptionAlg) {
        this.requestObjectEncryptionAlg = requestObjectEncryptionAlg;
    }

    public String getRequestObjectEncryptionEnc() {
        return requestObjectEncryptionEnc;
    }

    public void setRequestObjectEncryptionEnc(String requestObjectEncryptionEnc) {
        this.requestObjectEncryptionEnc = requestObjectEncryptionEnc;
    }

    public String getTokenEndpointAuthMethod() {
        return tokenEndpointAuthMethod;
    }

    public void setTokenEndpointAuthMethod(String tokenEndpointAuthMethod) {
        this.tokenEndpointAuthMethod = tokenEndpointAuthMethod;
    }

    public String getTokenEndpointAuthSigningAlg() {
        return tokenEndpointAuthSigningAlg;
    }

    public void setTokenEndpointAuthSigningAlg(String tokenEndpointAuthSigningAlg) {
        this.tokenEndpointAuthSigningAlg = tokenEndpointAuthSigningAlg;
    }

    public String getDefaultMaxAge() {
        return defaultMaxAge;
    }

    public void setDefaultMaxAge(String defaultMaxAge) {
        this.defaultMaxAge = defaultMaxAge;
    }

    public String getRequireAuthTime() {
        return requireAuthTime;
    }

    public void setRequireAuthTime(String requireAuthTime) {
        this.requireAuthTime = requireAuthTime;
    }

    public String getDefaultAcrValues() {
        return defaultAcrValues;
    }

    public void setDefaultAcrValues(String defaultAcrValues) {
        this.defaultAcrValues = defaultAcrValues;
    }

    public String getInitiateLoginUri() {
        return initiateLoginUri;
    }

    public void setInitiateLoginUri(String initiateLoginUri) {
        this.initiateLoginUri = initiateLoginUri;
    }

    public List<String> getRequestUris() {
        return requestUris;
    }

    public void setRequestUris(List<String> requestUris) {
        this.requestUris = requestUris;
    }

    public String getSoftwareStatement() {
        return softwareStatement;
    }

    public void setSoftwareStatement(String softwareStatement) {
        this.softwareStatement = softwareStatement;
    }

    public String getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(String softwareId) {
        this.softwareId = softwareId;
    }
}
