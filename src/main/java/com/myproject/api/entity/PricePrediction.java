package com.myproject.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_predictions", indexes = {
    @Index(name = "idx_crop_region_pred", columnList = "crop,region,prediction_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricePrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String crop;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private LocalDateTime predictionDate;

    @Column(nullable = false)
    private BigDecimal predictedPrice;

    @Column(nullable = false)
    private BigDecimal confidenceScore;

    private BigDecimal lowerBound;
    private BigDecimal upperBound;

    private String predictionModel; // ARIMA, LinearRegression, etc.

    @Column(nullable = false)
    private LocalDateTime predictionGeneratedAt;

    private BigDecimal actualPrice;

    @Column(length = 500)
    private String insights;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

