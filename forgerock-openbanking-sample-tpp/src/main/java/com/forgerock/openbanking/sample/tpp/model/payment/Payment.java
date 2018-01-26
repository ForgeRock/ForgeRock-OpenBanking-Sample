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

import uk.org.openbanking.datamodel.payment.OBInitiation1;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.OBTransactionIndividualStatus1Code;

import java.util.Date;


/**
 * Representation of a payment.
 */
public class Payment {
    public String id;

    public String paymentId;
    public String paymentSubmissionId;
    public OBTransactionIndividualStatus1Code status;
    public OBInitiation1 initiation;
    public OBRisk1 risk;
    public String accountId;
    public String userId;
    public String pispId;

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

    public OBTransactionIndividualStatus1Code getStatus() {
        return status;
    }

    public void setStatus(OBTransactionIndividualStatus1Code status) {
        this.status = status;
    }

    public OBInitiation1 getInitiation() {
        return initiation;
    }

    public void setInitiation(OBInitiation1 initiation) {
        this.initiation = initiation;
    }

    public OBRisk1 getRisk() {
        return risk;
    }

    public void setRisk(OBRisk1 risk) {
        this.risk = risk;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPispId() {
        return pispId;
    }

    public void setPispId(String pispId) {
        this.pispId = pispId;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", paymentSubmissionId='" + paymentSubmissionId + '\'' +
                ", status=" + status +
                ", initiation=" + initiation +
                ", risk=" + risk +
                ", accountId='" + accountId + '\'' +
                ", userId='" + userId + '\'' +
                ", pispId='" + pispId + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
