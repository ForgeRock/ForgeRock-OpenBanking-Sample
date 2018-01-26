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
package com.forgerock.openbanking.sample.tpp.model.payment;

import org.springframework.data.annotation.Id;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetupResponse1;

import java.util.Date;

/**
 * Payment setup entity for storing in DB
 */
public class PaymentSetup {
    @Id
    public String id;
    public String paymentId;
    public String financialId;

    public OBPaymentSetup1 paymentSetupRequest;
    public OBPaymentSetupResponse1 paymentSetupResponse;
    public String customerLastLoggedTime;
    public String customerIPAddress;
    public String idempotencyKey;
    public String interactionId;

    public Date created;
    public Date updated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getFinancialId() {
        return financialId;
    }

    public void setFinancialId(String financialId) {
        this.financialId = financialId;
    }

    public OBPaymentSetup1 getPaymentSetupRequest() {
        return paymentSetupRequest;
    }

    public void setPaymentSetupRequest(OBPaymentSetup1 paymentSetupRequest) {
        this.paymentSetupRequest = paymentSetupRequest;
    }

    public OBPaymentSetupResponse1 getPaymentSetupResponse() {
        return paymentSetupResponse;
    }

    public void setPaymentSetupResponse(OBPaymentSetupResponse1 paymentSetupResponse) {
        this.paymentSetupResponse = paymentSetupResponse;
    }

    public String getCustomerLastLoggedTime() {
        return customerLastLoggedTime;
    }

    public void setCustomerLastLoggedTime(String customerLastLoggedTime) {
        this.customerLastLoggedTime = customerLastLoggedTime;
    }

    public String getCustomerIPAddress() {
        return customerIPAddress;
    }

    public void setCustomerIPAddress(String customerIPAddress) {
        this.customerIPAddress = customerIPAddress;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(String interactionId) {
        this.interactionId = interactionId;
    }

    @Override
    public String toString() {
        return "PaymentSetup{" +
                "id=" + id +
                ", paymentId='" + paymentId + '\'' +
                ", paymentSetupRequest=" + paymentSetupRequest +
                ", paymentSetupResponse=" + paymentSetupResponse +
                ", customerLastLoggedTime='" + customerLastLoggedTime + '\'' +
                ", customerIPAddress='" + customerIPAddress + '\'' +
                ", idempotencyKey='" + idempotencyKey + '\'' +
                ", interactionId='" + interactionId + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
