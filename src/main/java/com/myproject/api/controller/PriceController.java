package com.myproject.api.controller;

import com.myproject.api.dto.PriceDTO;
import com.myproject.api.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prices")
@Slf4j
@Tag(name = "Prices", description = "Price management APIs")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @PostMapping
    @Operation(summary = "Add new price data")
    public ResponseEntity<PriceDTO> addPrice(@Valid @RequestBody PriceDTO priceDTO) {
        log.info("Adding price for crop: {}, region: {}", priceDTO.getCrop(), priceDTO.getRegion());
        PriceDTO savedPrice = priceService.addPrice(priceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPrice);
    }

    @GetMapping("/search")
    @Operation(summary = "Search prices by crop and region")
    public ResponseEntity<Page<PriceDTO>> searchPrices(
            @RequestParam(required = false) String crop,
            @RequestParam(required = false) String region,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PriceDTO> prices = priceService.searchPrices(crop, region, pageable);
        return ResponseEntity.ok(prices);
    }

    @GetMapping("/history")
    @Operation(summary = "Get price history for a crop-region combination")
    public ResponseEntity<List<PriceDTO>> getPriceHistory(
            @RequestParam String crop,
            @RequestParam String region,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<PriceDTO> history = priceService.getPriceHistory(crop, region, startDate, endDate);
        return ResponseEntity.ok(history);
    }
}

