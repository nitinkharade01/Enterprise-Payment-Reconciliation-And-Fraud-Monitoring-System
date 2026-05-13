/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.nitin.payment.fraud.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value="fraud_rules")
public class FraudRule {
    @Id
    private String id;
    private String name;
    private String expression;
    private boolean active = true;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getExpression() {
        return this.expression;
    }

    @Generated
    public boolean isActive() {
        return this.active;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Generated
    public void setActive(boolean active) {
        this.active = active;
    }
}
