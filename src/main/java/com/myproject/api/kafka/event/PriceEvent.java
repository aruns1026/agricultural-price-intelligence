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
public class PriceEvent {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("crop")
    private String crop;

    @JsonProperty("region")
    private String region;

    @JsonProperty("district")
    private String district;

    @JsonProperty("state")
    private String state;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("market")
    private String market;

    @JsonProperty("source")
    private String source; // INDIAN, US_IMPORT

    @JsonProperty("price_date")
    private LocalDateTime priceDate;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("notes")
    private String notes;
}

