package com.myproject.api.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@EnableKafka
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(KafkaAdmin.AdminClientProperty.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    }

    @Bean
    public NewTopic priceEventsTopic() {
        return new NewTopic("price-events", 3, (short) 1);
    }

    @Bean
    public NewTopic importDataEventsTopic() {
        return new NewTopic("import-data-events", 3, (short) 1);
    }

    @Bean
    public NewTopic alertEventsTopic() {
        return new NewTopic("alert-events", 3, (short) 1);
    }
}

