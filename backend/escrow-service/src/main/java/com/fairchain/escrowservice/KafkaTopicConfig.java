package com.fairchain.escrowservice;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${fairchain.kafka.topic.payment-released}")
    private String paymentReleasedTopic;

    @Bean
    public NewTopic paymentReleasedTopic() {
        return TopicBuilder.name(paymentReleasedTopic).partitions(3).replicas(1).build();
    }
}
