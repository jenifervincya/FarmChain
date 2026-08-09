package com.fairchain.batchservice;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Implements POST /api/v1/batches, GET /api/v1/batches/{trackingCode},
 * and GET /api/v1/price-journey/{trackingCode} per Section 4.1.
 */
@RestController
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping("/api/v1/batches")
    public ResponseEntity<BatchResponse> registerBatch(@Valid @RequestBody BatchRequest request) {
        BatchResponse response = batchService.registerBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/v1/batches/{trackingCode}")
    public ResponseEntity<BatchDetailResponse> getBatch(@PathVariable String trackingCode) {
        BatchDetailResponse response = batchService.getBatchByTrackingCode(trackingCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/price-journey/{trackingCode}")
    public ResponseEntity<PriceJourneyResponse> getPriceJourney(@PathVariable String trackingCode) {
        PriceJourneyResponse response = batchService.getPriceJourney(trackingCode);
        return ResponseEntity.ok(response);
    }
}
