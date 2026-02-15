package com.myproject.api.dto;

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
public class PriceDTO {

    private Long id;

    private String crop;

    private String region;

    private String district;

    private String state;

    private BigDecimal price;

    private String unit;

    private String market;

    private String source;

    private LocalDateTime priceDate;

    private BigDecimal previousDayPrice;

    private BigDecimal priceChangePercentage;

    @JsonProperty("weeklyAverage")
    private BigDecimal weeklyAveragePrice;

    @JsonProperty("monthlyAverage")
    private BigDecimal monthlyAveragePrice;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

