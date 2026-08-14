package com.fairchain.escrowservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * NOT in Section 4.1 — added at Archana's request so the Farmer
 * Dashboard's Payment Status section can read escrow state.
 * Flagged: needs adding to Section 4.1 officially, same pattern as
 * the farmers and notifications endpoints.
 */
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionRepository repository;

    public TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{batchId}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable String batchId) {
        Transaction transaction = repository.findByBatchId(batchId)
                .orElseThrow(() -> new TransactionNotFoundException(batchId));

        TransactionResponse response = new TransactionResponse(
                transaction.getBatchId(),
                transaction.getFarmerId(),
                transaction.getBuyerId(),
                transaction.getAmount(),
                transaction.getEscrowStatus(),
                transaction.getReleasedAt()
        );
        return ResponseEntity.ok(response);
    }
}
