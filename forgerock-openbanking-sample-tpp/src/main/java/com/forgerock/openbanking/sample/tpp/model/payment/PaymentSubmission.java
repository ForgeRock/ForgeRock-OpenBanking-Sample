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

import uk.org.openbanking.datamodel.payment.paymentsubmission.OBPaymentSubmission1;
import uk.org.openbanking.datamodel.payment.paymentsubmission.OBPaymentSubmissionResponse1;

import java.util.Date;

/**
 * Payment submission entity for storing in DB
 */
public class PaymentSubmission {
    public String id;
    public String paymentId;
    public String paymentSubmissionId;

    public OBPaymentSubmission1 paymentSubmission;
    public OBPaymentSubmissionResponse1 paymentSubmissionResponse;
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

    public String getPaymentSubmissionId() {
        return paymentSubmissionId;
    }

    public void setPaymentSubmissionId(String paymentSubmissionId) {
        this.paymentSubmissionId = paymentSubmissionId;
    }

    public OBPaymentSubmission1 getPaymentSubmission() {
        return paymentSubmission;
    }

    public void setPaymentSubmission(OBPaymentSubmission1 paymentSubmission) {
        this.paymentSubmission = paymentSubmission;
    }

    public OBPaymentSubmissionResponse1 getPaymentSubmissionResponse() {
        return paymentSubmissionResponse;
    }

    public void setPaymentSubmissionResponse(OBPaymentSubmissionResponse1 paymentSubmissionResponse) {
        this.paymentSubmissionResponse = paymentSubmissionResponse;
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
        return "PaymentSubmission{" +
                "id=" + id +
                ", paymentId='" + paymentId + '\'' +
                ", paymentSubmissionId='" + paymentSubmissionId + '\'' +
                ", paymentSubmission=" + paymentSubmission +
                ", paymentSubmissionResponse=" + paymentSubmissionResponse +
                ", customerLastLoggedTime='" + customerLastLoggedTime + '\'' +
                ", customerIPAddress='" + customerIPAddress + '\'' +
                ", idempotencyKey='" + idempotencyKey + '\'' +
                ", interactionId='" + interactionId + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
