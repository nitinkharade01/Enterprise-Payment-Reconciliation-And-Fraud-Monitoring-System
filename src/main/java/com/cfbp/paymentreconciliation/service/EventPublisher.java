package com.cfbp.paymentreconciliation.service;

public interface EventPublisher {
    void publish(String topic, Object event);
}
