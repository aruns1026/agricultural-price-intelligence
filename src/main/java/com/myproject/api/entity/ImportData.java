package com.myproject.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "import_data", indexes = {
    @Index(name = "idx_crop_import_date", columnList = "crop,import_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String crop;

    @Column(nullable = false)
    private String sourceCountry;

    @Column(nullable = false)
    private LocalDateTime importDate;

    @Column(nullable = false)
    private Long quantityTons;

    @Column(nullable = false)
    private BigDecimal pricePerTon;

    @Column(nullable = false)
    private BigDecimal totalValue;

    private String port;

    @Column(length = 500)
    private String description;

    private String impactRegion; // Which Indian region is impacted

    @Enumerated(EnumType.STRING)
    private ImpactLevel impactLevel; // LOW, MEDIUM, HIGH

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

    public enum ImpactLevel {
        LOW, MEDIUM, HIGH
    }
}

