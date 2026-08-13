package com.fairchain.auctionservice;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${fairchain.kafka.topic.sale-confirmed}")
    private String saleConfirmedTopic;

    @Value("${fairchain.kafka.topic.price-deviation}")
    private String priceDeviationTopic;

    @Bean
    public NewTopic saleConfirmedTopic() {
        return TopicBuilder.name(saleConfirmedTopic).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic priceDeviationTopic() {
        return TopicBuilder.name(priceDeviationTopic).partitions(3).replicas(1).build();
    }
}
