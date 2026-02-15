package com.myproject.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "prices", indexes = {
    @Index(name = "idx_crop_region_date", columnList = "crop,region,price_date"),
    @Index(name = "idx_date_range", columnList = "price_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String crop;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String unit; // kg, quintal, ton

    @Column(nullable = false)
    private String market;

    private String source; // INDIAN, US_IMPORT, OTHER

    @Column(nullable = false)
    private LocalDateTime priceDate;

    private BigDecimal previousDayPrice;

    private BigDecimal weeklyAveragePrice;

    private BigDecimal monthlyAveragePrice;

    @Column(precision = 5, scale = 2)
    private BigDecimal priceChangePercentage;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

