package com.fairchain.batchservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Internal-only endpoint per Section 4.3: "restricted to internal
 * service-to-service calls." No real access restriction implemented
 * yet (e.g. internal network only, or checking the Section 4.4 shared
 * API key) — flagged: this needs proper internal-only enforcement
 * before it's production-safe, currently just a plain public GET.
 */
@RestController
@RequestMapping("/api/v1/internal/price-history")
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    public PriceHistoryController(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<PriceHistoryResponse>> getPriceHistory(
            @RequestParam String crop,
            @RequestParam String region) {
        List<PriceHistoryResponse> response = priceHistoryService.getPriceHistory(crop, region);
        return ResponseEntity.ok(response);
    }
}
