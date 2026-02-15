package com.myproject.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_alerts", indexes = {
    @Index(name = "idx_user_alert", columnList = "user_id,enabled"),
    @Index(name = "idx_alert_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String crop;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private BigDecimal triggerPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType alertType; // PRICE_ABOVE, PRICE_BELOW, PRICE_CHANGE

    @Column(nullable = false)
    private BigDecimal triggerValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status;

    @Column(nullable = false)
    private Boolean enabled;

    private LocalDateTime lastTriggered;

    @Column(nullable = false)
    private LocalDateTime createdAt;

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

    public enum AlertType {
        PRICE_ABOVE,
        PRICE_BELOW,
        PRICE_CHANGE
    }

    public enum AlertStatus {
        ACTIVE,
        TRIGGERED,
        INACTIVE
    }
}

