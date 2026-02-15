package com.myproject.api.kafka.producer;

import com.myproject.api.kafka.event.PriceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PriceEventProducer {

    private final KafkaTemplate<String, PriceEvent> kafkaTemplate;

    public PriceEventProducer(KafkaTemplate<String, PriceEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPriceEvent(PriceEvent event) {
        try {
            if (event.getEventId() == null) {
                event.setEventId(UUID.randomUUID().toString());
            }

            kafkaTemplate.send("price-events", event.getEventId(), event);
            log.info("Price event published: {} for crop: {} in region: {}",
                event.getEventId(), event.getCrop(), event.getRegion());
        } catch (Exception e) {
            log.error("Failed to publish price event", e);
            throw new RuntimeException("Failed to publish price event", e);
        }
    }
}

