package com.myproject.api.controller;

import com.myproject.api.service.AgriculturalAIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
@Tag(name = "Agricultural AI Chat", description = "AI-powered agricultural insights")
public class ChatController {

    private final AgriculturalAIService aiService;

    public ChatController(AgriculturalAIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/advice")
    @Operation(summary = "Get AI-powered agricultural advice")
    public ResponseEntity<String> getAgriculturalAdvice(
            @RequestParam String crop,
            @RequestParam String region,
            @RequestParam String question) {

        log.info("Getting agricultural advice for crop: {}, region: {}", crop, region);
        String advice = aiService.getAgriculturalAdvice(crop, region, question);
        return ResponseEntity.ok(advice);
    }

    @GetMapping("/import-impact")
    @Operation(summary = "Get analysis of US import impact on Indian agriculture")
    public ResponseEntity<String> getImportImpactAnalysis(
            @RequestParam String crop,
            @RequestParam(defaultValue = "MEDIUM") String impactLevel) {

        log.info("Analyzing US import impact on {} market", crop);
        String analysis = aiService.getImportImpactAnalysis(crop, impactLevel);
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/market-trend")
    @Operation(summary = "Analyze market trend for a crop in a region")
    public ResponseEntity<String> analyzeMarketTrend(
            @RequestParam String crop,
            @RequestParam String region) {

        log.info("Analyzing market trend for crop: {}, region: {}", crop, region);
        String trend = aiService.analyzeMarketTrend(crop, region);
        return ResponseEntity.ok(trend);
    }
}

