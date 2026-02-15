package com.myproject.api.dto;

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
public class PricePredictionDTO {

    private Long id;

    private String crop;

    private String region;

    private LocalDateTime predictionDate;

    private BigDecimal predictedPrice;

    private BigDecimal confidenceScore;

    private BigDecimal lowerBound;

    private BigDecimal upperBound;

    private String predictionModel;

    private LocalDateTime predictionGeneratedAt;

    private BigDecimal actualPrice;

    private String insights;
}

