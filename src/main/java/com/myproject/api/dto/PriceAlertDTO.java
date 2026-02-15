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
public class PriceAlertDTO {

    private Long id;

    private Long userId;

    private String crop;

    private String region;

    private BigDecimal triggerPrice;

    @JsonProperty("alertType")
    private String alertType;

    private BigDecimal triggerValue;

    private String status;

    private Boolean enabled;

    private LocalDateTime lastTriggered;

    private LocalDateTime createdAt;
}

