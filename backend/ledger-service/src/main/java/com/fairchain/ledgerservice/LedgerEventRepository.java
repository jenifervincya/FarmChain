package com.fairchain.ledgerservice;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LedgerEventRepository extends JpaRepository<LedgerEvent, Long> {

    List<LedgerEvent> findByBatchIdOrderByTimestampAsc(String batchId);

    Optional<LedgerEvent> findFirstByBatchIdOrderByTimestampDesc(String batchId);
}
