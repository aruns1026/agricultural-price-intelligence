package com.myproject.api.repository;

import com.myproject.api.entity.ImportData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ImportDataRepository extends JpaRepository<ImportData, Long> {

    Page<ImportData> findByCrop(String crop, Pageable pageable);

    Page<ImportData> findBySourceCountry(String sourceCountry, Pageable pageable);

    List<ImportData> findByCropAndImportDateAfterOrderByImportDateDesc(String crop, LocalDateTime date);

    List<ImportData> findByImpactRegionOrderByImportDateDesc(String impactRegion);

    List<ImportData> findByCropAndImpactLevelOrderByImportDateDesc(String crop, ImportData.ImpactLevel impactLevel);

    Page<ImportData> findByImportDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}

