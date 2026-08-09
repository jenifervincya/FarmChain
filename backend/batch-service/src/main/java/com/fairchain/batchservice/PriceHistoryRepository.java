package com.fairchain.batchservice;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    List<PriceHistory> findByCropAndRegionOrderByRecordedAtDesc(String crop, String region);
}
