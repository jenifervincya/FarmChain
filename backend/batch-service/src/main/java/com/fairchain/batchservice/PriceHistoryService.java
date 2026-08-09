package com.fairchain.batchservice;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceHistoryService {

    private final PriceHistoryRepository repository;

    public PriceHistoryService(PriceHistoryRepository repository) {
        this.repository = repository;
    }

    public List<PriceHistoryResponse> getPriceHistory(String crop, String region) {
        return repository.findByCropAndRegionOrderByRecordedAtDesc(crop, region)
                .stream()
                .map(p -> new PriceHistoryResponse(
                        p.getCrop(), p.getRegion(), p.getPrice(), p.getSource(), p.getRecordedAt()
                ))
                .toList();
    }
}
