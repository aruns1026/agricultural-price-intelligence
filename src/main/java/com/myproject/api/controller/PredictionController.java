package com.myproject.api.controller;

import com.myproject.api.dto.PricePredictionDTO;
import com.myproject.api.service.PricePredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/predictions")
@Slf4j
@Tag(name = "Price Predictions", description = "Price prediction APIs")
public class PredictionController {

    private final PricePredictionService predictionService;

    public PredictionController(PricePredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping("/predict")
    @PreAuthorize("hasAnyRole('FARMER', 'ANALYST', 'ADMIN')")
    @Operation(summary = "Predict price for crop-region combination")
    public ResponseEntity<PricePredictionDTO> predictPrice(
            @RequestParam String crop,
            @RequestParam String region) {

        log.info("Predicting price for crop: {}, region: {}", crop, region);
        PricePredictionDTO prediction = predictionService.predictPrice(crop, region);
        return ResponseEntity.ok(prediction);
    }

    @GetMapping
    @Operation(summary = "Get price predictions")
    public ResponseEntity<List<PricePredictionDTO>> getPredictions(
            @RequestParam String crop,
            @RequestParam String region) {

        List<PricePredictionDTO> predictions = predictionService.getPredictions(crop, region);
        return ResponseEntity.ok(predictions);
    }
}

