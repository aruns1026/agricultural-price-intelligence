package com.myproject.api.repository;

import com.myproject.api.entity.PriceAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {

    Page<PriceAlert> findByUserId(Long userId, Pageable pageable);

    List<PriceAlert> findByUserIdAndEnabled(Long userId, Boolean enabled);

    Page<PriceAlert> findByUserIdAndEnabled(Long userId, Boolean enabled, Pageable pageable);

    List<PriceAlert> findByCropAndRegionAndStatus(String crop, String region, PriceAlert.AlertStatus status);

    List<PriceAlert> findByEnabledTrue();

    int countByUserId(Long userId);
}

