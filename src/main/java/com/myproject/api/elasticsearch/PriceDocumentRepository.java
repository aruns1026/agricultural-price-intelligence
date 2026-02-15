package com.myproject.api.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceDocumentRepository extends ElasticsearchRepository<PriceDocument, String> {

    Page<PriceDocument> findByCrop(String crop, Pageable pageable);

    Page<PriceDocument> findByRegion(String region, Pageable pageable);

    Page<PriceDocument> findByCropAndRegion(String crop, String region, Pageable pageable);

    Page<PriceDocument> findByCropAndRegionAndPriceDateBetween(
        String crop,
        String region,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable
    );

    List<PriceDocument> findByCropAndRegionAndSourceOrderByPriceDateDesc(
        String crop,
        String region,
        String source
    );

    Page<PriceDocument> findBySource(String source, Pageable pageable);

    List<PriceDocument> findByCropOrderByPriceDateDesc(String crop);
}

