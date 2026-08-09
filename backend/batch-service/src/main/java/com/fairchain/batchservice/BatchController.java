package com.fairchain.batchservice;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Implements POST /api/v1/batches per Section 4.1.
 * Gateway is expected to route /api/v1/** here.
 */
@RestController
@RequestMapping("/api/v1/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping
    public ResponseEntity<BatchResponse> registerBatch(@Valid @RequestBody BatchRequest request) {
        BatchResponse response = batchService.registerBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
