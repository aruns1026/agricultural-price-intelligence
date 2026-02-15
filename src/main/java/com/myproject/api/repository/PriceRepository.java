package com.myproject.api.repository;

import com.myproject.api.entity.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    Page<Price> findByCrop(String crop, Pageable pageable);

    Page<Price> findByRegion(String region, Pageable pageable);

    Page<Price> findByCropAndRegion(String crop, String region, Pageable pageable);

    List<Price> findByCropAndRegionAndPriceDateBetweenOrderByPriceDateDesc(
        String crop,
        String region,
        LocalDateTime startDate,
        LocalDateTime endDate
    );

    @Query("SELECT p FROM Price p WHERE p.crop = :crop AND p.region = :region ORDER BY p.priceDate DESC LIMIT 1")
    Price findLatestPrice(@Param("crop") String crop, @Param("region") String region);

    Page<Price> findBySource(String source, Pageable pageable);

    List<Price> findByCropOrderByPriceDateDesc(String crop);

    @Query("SELECT AVG(p.price) FROM Price p WHERE p.crop = :crop AND p.region = :region AND p.priceDate >= :startDate")
    Double calculateAveragePrice(
        @Param("crop") String crop,
        @Param("region") String region,
        @Param("startDate") LocalDateTime startDate
    );
}

