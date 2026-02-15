package com.myproject.api.service;

import com.myproject.api.dto.PriceDTO;
import com.myproject.api.elasticsearch.PriceDocument;
import com.myproject.api.elasticsearch.PriceDocumentRepository;
import com.myproject.api.entity.Price;
import com.myproject.api.kafka.event.PriceEvent;
import com.myproject.api.kafka.producer.PriceEventProducer;
import com.myproject.api.repository.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PriceService {

    private final PriceRepository priceRepository;
    private final PriceDocumentRepository priceDocumentRepository;
    private final PriceEventProducer priceEventProducer;

    public PriceService(PriceRepository priceRepository,
                       PriceDocumentRepository priceDocumentRepository,
                       PriceEventProducer priceEventProducer) {
        this.priceRepository = priceRepository;
        this.priceDocumentRepository = priceDocumentRepository;
        this.priceEventProducer = priceEventProducer;
    }

    @Transactional
    public PriceDTO addPrice(PriceDTO priceDTO) {
        Price price = Price.builder()
                .crop(priceDTO.getCrop())
                .region(priceDTO.getRegion())
                .district(priceDTO.getDistrict())
                .state(priceDTO.getState())
                .price(priceDTO.getPrice())
                .unit(priceDTO.getUnit())
                .market(priceDTO.getMarket())
                .source(priceDTO.getSource())
                .priceDate(priceDTO.getPriceDate())
                .notes(priceDTO.getNotes())
                .build();

        Price savedPrice = priceRepository.save(price);

        // Publish event to Kafka
        PriceEvent event = PriceEvent.builder()
                .crop(savedPrice.getCrop())
                .region(savedPrice.getRegion())
                .district(savedPrice.getDistrict())
                .state(savedPrice.getState())
                .price(savedPrice.getPrice())
                .unit(savedPrice.getUnit())
                .market(savedPrice.getMarket())
                .source(savedPrice.getSource())
                .priceDate(savedPrice.getPriceDate())
                .timestamp(LocalDateTime.now())
                .notes(savedPrice.getNotes())
                .build();

        priceEventProducer.publishPriceEvent(event);

        return mapToDTO(savedPrice);
    }

    public Page<PriceDTO> searchPrices(String crop, String region, Pageable pageable) {
        Page<PriceDocument> documents;

        if (crop != null && region != null) {
            documents = priceDocumentRepository.findByCropAndRegion(crop, region, pageable);
        } else if (crop != null) {
            documents = priceDocumentRepository.findByCrop(crop, pageable);
        } else if (region != null) {
            documents = priceDocumentRepository.findByRegion(region, pageable);
        } else {
            throw new IllegalArgumentException("At least crop or region must be specified");
        }

        return documents.map(this::mapDocumentToDTO);
    }

    public List<PriceDTO> getPriceHistory(String crop, String region, LocalDateTime startDate, LocalDateTime endDate) {
        List<PriceDocument> documents = priceDocumentRepository
                .findByCropAndRegionAndPriceDateBetween(crop, region, startDate, endDate,
                    org.springframework.data.domain.PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();

        return documents.stream()
                .map(this::mapDocumentToDTO)
                .collect(Collectors.toList());
    }

    private PriceDTO mapToDTO(Price price) {
        return PriceDTO.builder()
                .id(price.getId())
                .crop(price.getCrop())
                .region(price.getRegion())
                .district(price.getDistrict())
                .state(price.getState())
                .price(price.getPrice())
                .unit(price.getUnit())
                .market(price.getMarket())
                .source(price.getSource())
                .priceDate(price.getPriceDate())
                .previousDayPrice(price.getPreviousDayPrice())
                .weeklyAveragePrice(price.getWeeklyAveragePrice())
                .monthlyAveragePrice(price.getMonthlyAveragePrice())
                .priceChangePercentage(price.getPriceChangePercentage())
                .notes(price.getNotes())
                .createdAt(price.getCreatedAt())
                .updatedAt(price.getUpdatedAt())
                .build();
    }

    private PriceDTO mapDocumentToDTO(PriceDocument doc) {
        return PriceDTO.builder()
                .id(Long.parseLong(doc.getId()))
                .crop(doc.getCrop())
                .region(doc.getRegion())
                .district(doc.getDistrict())
                .state(doc.getState())
                .price(doc.getPrice())
                .unit(doc.getUnit())
                .market(doc.getMarket())
                .source(doc.getSource())
                .priceDate(doc.getPriceDate())
                .previousDayPrice(doc.getPreviousDayPrice())
                .weeklyAveragePrice(doc.getWeeklyAveragePrice())
                .monthlyAveragePrice(doc.getMonthlyAveragePrice())
                .priceChangePercentage(doc.getPriceChangePercentage())
                .notes(doc.getNotes())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }
}

