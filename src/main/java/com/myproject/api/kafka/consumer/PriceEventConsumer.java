package com.myproject.api.kafka.consumer;

import com.myproject.api.elasticsearch.PriceDocument;
import com.myproject.api.elasticsearch.PriceDocumentRepository;
import com.myproject.api.entity.Price;
import com.myproject.api.kafka.event.PriceEvent;
import com.myproject.api.repository.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PriceEventConsumer {

    private final PriceRepository priceRepository;
    private final PriceDocumentRepository priceDocumentRepository;

    public PriceEventConsumer(PriceRepository priceRepository,
                             PriceDocumentRepository priceDocumentRepository) {
        this.priceRepository = priceRepository;
        this.priceDocumentRepository = priceDocumentRepository;
    }

    @KafkaListener(topics = "price-events", groupId = "agricultural-price-group")
    public void consumePriceEvent(PriceEvent event) {
        try {
            log.info("Consuming price event: {} for crop: {} in region: {}",
                event.getEventId(), event.getCrop(), event.getRegion());

            // Save to PostgreSQL
            Price price = Price.builder()
                .crop(event.getCrop())
                .region(event.getRegion())
                .district(event.getDistrict())
                .state(event.getState())
                .price(event.getPrice())
                .unit(event.getUnit())
                .market(event.getMarket())
                .source(event.getSource())
                .priceDate(event.getPriceDate())
                .notes(event.getNotes())
                .build();

            Price savedPrice = priceRepository.save(price);

            // Index to Elasticsearch
            PriceDocument priceDoc = PriceDocument.builder()
                .id(savedPrice.getId().toString())
                .crop(savedPrice.getCrop())
                .region(savedPrice.getRegion())
                .district(savedPrice.getDistrict())
                .state(savedPrice.getState())
                .price(savedPrice.getPrice())
                .unit(savedPrice.getUnit())
                .market(savedPrice.getMarket())
                .source(savedPrice.getSource())
                .priceDate(savedPrice.getPriceDate())
                .notes(savedPrice.getNotes())
                .createdAt(savedPrice.getCreatedAt())
                .updatedAt(savedPrice.getUpdatedAt())
                .build();

            priceDocumentRepository.save(priceDoc);
            log.info("Price event processed and indexed: {}", event.getEventId());

        } catch (Exception e) {
            log.error("Failed to consume price event: {}", event.getEventId(), e);
        }
    }
}

