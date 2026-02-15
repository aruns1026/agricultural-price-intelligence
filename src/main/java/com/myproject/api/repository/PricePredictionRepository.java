package com.myproject.api.repository;

import com.myproject.api.entity.PricePrediction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PricePredictionRepository extends JpaRepository<PricePrediction, Long> {

    Page<PricePrediction> findByCrop(String crop, Pageable pageable);

    Page<PricePrediction> findByRegion(String region, Pageable pageable);

    Page<PricePrediction> findByCropAndRegion(String crop, String region, Pageable pageable);

    List<PricePrediction> findByCropAndRegionOrderByPredictionDateDesc(String crop, String region);

    Optional<PricePrediction> findFirstByCropAndRegionOrderByPredictionDateDesc(String crop, String region);

    List<PricePrediction> findByCropAndRegionAndPredictionDateAfterOrderByPredictionDateDesc(
        String crop,
        String region,
        LocalDateTime date
    );

    List<PricePrediction> findByPredictionDateBetweenOrderByPredictionDateDesc(
        LocalDateTime startDate,
        LocalDateTime endDate
    );
}

