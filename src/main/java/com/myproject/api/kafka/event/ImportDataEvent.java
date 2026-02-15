package com.myproject.api.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportDataEvent {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("crop")
    private String crop;

    @JsonProperty("source_country")
    private String sourceCountry;

    @JsonProperty("import_date")
    private LocalDateTime importDate;

    @JsonProperty("quantity_tons")
    private Long quantityTons;

    @JsonProperty("price_per_ton")
    private BigDecimal pricePerTon;

    @JsonProperty("total_value")
    private BigDecimal totalValue;

    @JsonProperty("port")
    private String port;

    @JsonProperty("impact_region")
    private String impactRegion;

    @JsonProperty("impact_level")
    private String impactLevel;

    @JsonProperty("description")
    private String description;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}

