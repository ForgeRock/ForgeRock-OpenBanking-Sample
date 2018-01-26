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

import org.springframework.data.annotation.Id;

public class OIDCState {

    @Id
    private String state;
    private String intentId;
    private String aspspId;

    public String getState() {
        return state;
    }

    public String getIntentId() {
        return intentId;
    }

    public String getAspspId() {
        return aspspId;
    }

    public OIDCState intentId(String intentId) {
        this.intentId = intentId;
        return this;
    }

    public OIDCState aspspId(String aspspId) {
        this.aspspId = aspspId;
        return this;
    }
}
