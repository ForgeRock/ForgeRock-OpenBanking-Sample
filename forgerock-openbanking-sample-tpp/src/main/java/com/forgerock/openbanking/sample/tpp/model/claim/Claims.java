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
package com.forgerock.openbanking.sample.tpp.model.claim;

import net.minidev.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Models OpenID Connect claims that are requested in an authorize request.
 */
public class Claims {

    private final Map<String, Claim> userInfoClaims;
    private final Map<String, Claim> idTokenClaims;

    /**
     * Creates a Claims object.
     *
     * @param userInfoClaims The userinfo claims.
     * @param idTokenClaims  The id_token claims.
     */
    public Claims(Map<String, Claim> userInfoClaims, Map<String, Claim> idTokenClaims) {
        this.userInfoClaims = userInfoClaims;
        this.idTokenClaims = idTokenClaims;
    }

    /**
     * Gets the userinfo claims.
     *
     * @return The userinfo claims.
     */
    public Map<String, Claim> getUserInfoClaims() {
        return userInfoClaims;
    }

    /**
     * Gets the id_token claims.
     *
     * @return The id_token claims.
     */
    public Map<String, Claim> getIdTokenClaims() {
        return idTokenClaims;
    }

    /**
     * Gets all the claims for both userinfo and id_token claims.
     *
     * @return All claims.
     */
    public Map<String, Claim> getAllClaims() {
        Map<String, Claim> claims = new HashMap<>(userInfoClaims);
        claims.putAll(idTokenClaims);
        return claims;
    }

    public static Claims parseClaims(JSONObject claimsJson) {
        return new Claims(parseClaimsFrom("user_info", claimsJson), parseClaimsFrom("id_token", claimsJson));
    }

    private static Map<String, Claim> parseClaimsFrom(String key, JSONObject json) {
        Map<String, Claim> claims = new HashMap<>();
        if (json.containsKey(key)) {
            JSONObject claimsAsJson = (JSONObject) json.get(key);
            for (Object claimKey : claimsAsJson.keySet()) {
                claims.put((String) claimKey, Claim.parseClaim((JSONObject) claimsAsJson.get(claimKey)));
            }
        }
        return claims;
    }
}
