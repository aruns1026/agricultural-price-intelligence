package com.myproject.api.service;

import com.myproject.api.dto.PricePredictionDTO;
import com.myproject.api.entity.PricePrediction;
import com.myproject.api.entity.Price;
import com.myproject.api.repository.PricePredictionRepository;
import com.myproject.api.repository.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PricePredictionService {

    private final PricePredictionRepository predictionRepository;
    private final PriceRepository priceRepository;

    public PricePredictionService(PricePredictionRepository predictionRepository,
                                 PriceRepository priceRepository) {
        this.predictionRepository = predictionRepository;
        this.priceRepository = priceRepository;
    }

    @Transactional
    public PricePredictionDTO predictPrice(String crop, String region) {
        List<Price> historicalPrices = priceRepository
                .findByCropAndRegionAndPriceDateBetweenOrderByPriceDateDesc(
                    crop, region,
                    LocalDateTime.now().minusDays(90),
                    LocalDateTime.now()
                );

        if (historicalPrices.size() < 10) {
            throw new RuntimeException("Insufficient historical data for prediction");
        }

        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < historicalPrices.size(); i++) {
            regression.addData(i, historicalPrices.get(i).getPrice().doubleValue());
        }

        double predictedValue = regression.predict(historicalPrices.size());
        BigDecimal predictedPrice = BigDecimal.valueOf(predictedValue);

        // Calculate confidence score based on R-squared
        double rSquared = regression.getRSquare();
        BigDecimal confidenceScore = BigDecimal.valueOf(rSquared);

        PricePrediction prediction = PricePrediction.builder()
                .crop(crop)
                .region(region)
                .predictionDate(LocalDateTime.now().plusDays(7))
                .predictedPrice(predictedPrice)
                .confidenceScore(confidenceScore)
                .lowerBound(predictedPrice.multiply(BigDecimal.valueOf(0.95)))
                .upperBound(predictedPrice.multiply(BigDecimal.valueOf(1.05)))
                .predictionModel("Linear Regression")
                .predictionGeneratedAt(LocalDateTime.now())
                .build();

        PricePrediction saved = predictionRepository.save(prediction);
        return mapToDTO(saved);
    }

    public List<PricePredictionDTO> getPredictions(String crop, String region) {
        List<PricePrediction> predictions = predictionRepository
                .findByCropAndRegionOrderByPredictionDateDesc(crop, region);

        return predictions.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PricePredictionDTO mapToDTO(PricePrediction prediction) {
        return PricePredictionDTO.builder()
                .id(prediction.getId())
                .crop(prediction.getCrop())
                .region(prediction.getRegion())
                .predictionDate(prediction.getPredictionDate())
                .predictedPrice(prediction.getPredictedPrice())
                .confidenceScore(prediction.getConfidenceScore())
                .lowerBound(prediction.getLowerBound())
                .upperBound(prediction.getUpperBound())
                .predictionModel(prediction.getPredictionModel())
                .predictionGeneratedAt(prediction.getPredictionGeneratedAt())
                .actualPrice(prediction.getActualPrice())
                .insights(prediction.getInsights())
                .build();
    }
}

