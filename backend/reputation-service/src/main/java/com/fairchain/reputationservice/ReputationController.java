package com.fairchain.reputationservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implements GET /api/v1/reputation/{entityId} per Section 4.1.
 */
@RestController
@RequestMapping("/api/v1/reputation")
public class ReputationController {

    private final ReputationService reputationService;

    public ReputationController(ReputationService reputationService) {
        this.reputationService = reputationService;
    }

    @GetMapping("/{entityId}")
    public ResponseEntity<ReputationResponse> getReputation(@PathVariable String entityId) {
        ReputationResponse response = reputationService.getReputation(entityId);
        return ResponseEntity.ok(response);
    }
}
