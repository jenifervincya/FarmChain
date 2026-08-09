package com.fairchain.escrowservice;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByBatchId(String batchId);
}
