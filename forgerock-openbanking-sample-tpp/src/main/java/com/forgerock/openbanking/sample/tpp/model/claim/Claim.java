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

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Claim model, according to the section 5 of the openid connect standard.
 */
public class Claim {

    private boolean essential;
    private List<String> values;

    public Claim(boolean essential, String... values) {
        this.essential = essential;
        this.values = Arrays.asList(values);
    }

    public Claim(boolean essential, List<String> values) {
        this.essential = essential;
        this.values = values;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        if (values.size() == 1) {
            json.put("value", values.get(0));
        } else if (values.size() > 1) {
            JSONArray jsonValues = new JSONArray();
            jsonValues.addAll(values);
            json.put("values", jsonValues);
        }
        json.put("essential", essential);
        return json;
    }

    public boolean isEssential() {
        return essential;
    }

    public List<String> getValues() {
        return values;
    }

    public String getValue() {
        return values.get(0);
    }

    public static Claim parseClaim(JSONObject json) {
        boolean essential = false;
        List<String> values = new ArrayList<>();
        if (json.containsKey("essential")) {
            essential = (boolean) json.get("essential");
        }
        if (json.containsKey("value")) {
            values.add((String) json.get("value"));
        }
        if (json.containsKey("values")) {
            JSONArray array = (JSONArray) json.get("values");
            String[] valuesAsArray = (String[]) array.toArray();
            values.addAll(Arrays.asList(valuesAsArray));
        }
        return new Claim(essential, values);
    }
}
