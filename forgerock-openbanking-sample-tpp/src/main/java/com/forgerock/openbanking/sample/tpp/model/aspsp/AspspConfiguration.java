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
package com.forgerock.openbanking.sample.tpp.model.aspsp;

import com.forgerock.openbanking.sample.tpp.model.as.discovery.OIDCDiscoveryResponse;
import com.forgerock.openbanking.sample.tpp.model.as.registration.OIDCRegistrationResponse;
import org.springframework.data.annotation.Id;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksAccount;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment;

public class AspspConfiguration {

    @Id
    private String id;
    private String name;
    private String logo;
    private String financialId;
    private String ssa;
    private String wellKnownEndpoint;
    private String discoveryEndpoint;
    private String registrationEndpoint;
    private OIDCRegistrationResponse oidcRegistrationResponse;
    private OIDCDiscoveryResponse oidcDiscoveryResponse;
    private OBDiscoveryAPILinksPayment discoveryAPILinksPayment;
    private OBDiscoveryAPILinksAccount discoveryAPILinksAccount;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public String getFinancialId() {
        return financialId;
    }

    public void setFinancialId(String financialId) {
        this.financialId = financialId;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSsa() {
        return ssa;
    }

    public void setSsa(String ssa) {
        this.ssa = ssa;
    }

    public String getWellKnownEndpoint() {
        return wellKnownEndpoint;
    }

    public void setWellKnownEndpoint(String wellKnownEndpoint) {
        this.wellKnownEndpoint = wellKnownEndpoint;
    }

    public String getDiscoveryEndpoint() {
        return discoveryEndpoint;
    }

    public void setDiscoveryEndpoint(String discoveryEndpoint) {
        this.discoveryEndpoint = discoveryEndpoint;
    }

    public OIDCRegistrationResponse getOidcRegistrationResponse() {
        return oidcRegistrationResponse;
    }

    public void setOidcRegistrationResponse(OIDCRegistrationResponse oidcRegistrationResponse) {
        this.oidcRegistrationResponse = oidcRegistrationResponse;
    }

    public OIDCDiscoveryResponse getOidcDiscoveryResponse() {
        return oidcDiscoveryResponse;
    }

    public void setOidcDiscoveryResponse(OIDCDiscoveryResponse oidcDiscoveryResponse) {
        this.oidcDiscoveryResponse = oidcDiscoveryResponse;
    }

    public OBDiscoveryAPILinksPayment getDiscoveryAPILinksPayment() {
        return discoveryAPILinksPayment;
    }

    public OBDiscoveryAPILinksAccount getDiscoveryAPILinksAccount() {
        return discoveryAPILinksAccount;
    }

    public void setDiscoveryAPILinksPayment(OBDiscoveryAPILinksPayment discoveryAPILinksPayment) {
        this.discoveryAPILinksPayment = discoveryAPILinksPayment;
    }

    public void setDiscoveryAPILinksAccount(OBDiscoveryAPILinksAccount discoveryAPILinksAccount) {
        this.discoveryAPILinksAccount = discoveryAPILinksAccount;
    }

    public String getRegistrationEndpoint() {
        return registrationEndpoint;
    }

    public void setRegistrationEndpoint(String registrationEndpoint) {
        this.registrationEndpoint = registrationEndpoint;
    }

    @Override
    public String toString() {
        return "AspspConfiguration{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", financialId='" + financialId + '\'' +
                ", ssa='" + ssa + '\'' +
                ", wellKnownEndpoint='" + wellKnownEndpoint + '\'' +
                ", discoveryEndpoint='" + discoveryEndpoint + '\'' +
                ", registrationEndpoint='" + registrationEndpoint + '\'' +
                ", oidcRegistrationResponse=" + oidcRegistrationResponse +
                ", oidcDiscoveryResponse=" + oidcDiscoveryResponse +
                ", discoveryAPILinksPayment=" + discoveryAPILinksPayment +
                ", discoveryAPILinksAccount=" + discoveryAPILinksAccount +
                '}';
    }
}
