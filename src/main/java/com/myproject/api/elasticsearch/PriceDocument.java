package com.myproject.api.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(indexName = "prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String crop;

    @Field(type = FieldType.Keyword)
    private String region;

    @Field(type = FieldType.Keyword)
    private String district;

    @Field(type = FieldType.Keyword)
    private String state;

    @Field(type = FieldType.Scaled_Float, scalingFactor = 100)
    private BigDecimal price;

    @Field(type = FieldType.Keyword)
    private String unit;

    @Field(type = FieldType.Keyword)
    private String market;

    @Field(type = FieldType.Keyword)
    private String source;

    @Field(type = FieldType.Date)
    private LocalDateTime priceDate;

    @Field(type = FieldType.Scaled_Float, scalingFactor = 100)
    private BigDecimal previousDayPrice;

    @Field(type = FieldType.Scaled_Float, scalingFactor = 100)
    private BigDecimal weeklyAveragePrice;

    @Field(type = FieldType.Scaled_Float, scalingFactor = 100)
    private BigDecimal monthlyAveragePrice;

    @Field(type = FieldType.Scaled_Float, scalingFactor = 100)
    private BigDecimal priceChangePercentage;

    @Field(type = FieldType.Text)
    private String notes;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date)
    private LocalDateTime updatedAt;
}

