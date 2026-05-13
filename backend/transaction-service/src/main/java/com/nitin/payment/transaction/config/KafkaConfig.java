/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.kafka.clients.admin.NewTopic
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.kafka.config.TopicBuilder
 */
package com.nitin.payment.transaction.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(prefix="app.kafka", name={"enabled"}, havingValue="true", matchIfMissing=true)
public class KafkaConfig {
    @Bean
    NewTopic transactionCreatedTopic() {
        return TopicBuilder.name((String)"transaction-created-topic").partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic transactionStatusUpdatedTopic() {
        return TopicBuilder.name((String)"transaction-status-updated-topic").partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic fraudAlertTopic() {
        return TopicBuilder.name((String)"fraud-alert-topic").partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic reconciliationCompletedTopic() {
        return TopicBuilder.name((String)"reconciliation-completed-topic").partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic notificationTopic() {
        return TopicBuilder.name((String)"notification-topic").partitions(3).replicas(1).build();
    }
}
