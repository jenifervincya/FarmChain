package com.fairchain.ledgerservice;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Declares the 4 event topics from Section 4.2. Backend owns
 * Kafka topic creation/schema per Section 2.1/2.2.
 */
@Configuration
public class KafkaTopicConfig {

    @Value("${fairchain.kafka.topic.pickup-event}")
    private String pickupEventTopic;

    @Value("${fairchain.kafka.topic.transport-event}")
    private String transportEventTopic;

    @Value("${fairchain.kafka.topic.warehouse-event}")
    private String warehouseEventTopic;

    @Value("${fairchain.kafka.topic.delivery-event}")
    private String deliveryEventTopic;

    @Bean
    public NewTopic pickupEventTopic() {
        return TopicBuilder.name(pickupEventTopic).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic transportEventTopic() {
        return TopicBuilder.name(transportEventTopic).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic warehouseEventTopic() {
        return TopicBuilder.name(warehouseEventTopic).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic deliveryEventTopic() {
        return TopicBuilder.name(deliveryEventTopic).partitions(3).replicas(1).build();
    }
}
