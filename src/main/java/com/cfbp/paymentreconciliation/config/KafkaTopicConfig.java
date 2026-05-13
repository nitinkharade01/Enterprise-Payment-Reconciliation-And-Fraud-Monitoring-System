package com.cfbp.paymentreconciliation.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class KafkaTopicConfig {

    @Bean
    public NewTopic paymentCreatedTopic(@Value("${app.kafka.topics.payment-created}") String topic) {
        return TopicBuilder.name(topic).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic paymentValidatedTopic(@Value("${app.kafka.topics.payment-validated}") String topic) {
        return TopicBuilder.name(topic).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic reconciliationCompletedTopic(@Value("${app.kafka.topics.reconciliation-completed}") String topic) {
        return TopicBuilder.name(topic).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic fraudAlertTopic(@Value("${app.kafka.topics.fraud-alert}") String topic) {
        return TopicBuilder.name(topic).partitions(1).replicas(1).build();
    }
}
