/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.nitin.payment.fraud.repository;

import com.nitin.payment.fraud.model.FraudAlert;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FraudAlertRepository
extends MongoRepository<FraudAlert, String> {
    public long countByRiskStatus(String var1);
}
