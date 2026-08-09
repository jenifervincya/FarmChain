package com.fairchain.reputationservice;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReputationScoreRepository extends JpaRepository<ReputationScore, Long> {
    Optional<ReputationScore> findByEntityId(String entityId);
}
