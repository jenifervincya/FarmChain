package com.fairchain.reputationservice;

import org.springframework.stereotype.Service;

@Service
public class ReputationService {

    private final ReputationScoreRepository repository;

    public ReputationService(ReputationScoreRepository repository) {
        this.repository = repository;
    }

    public ReputationResponse getReputation(String entityId) {
        ReputationScore score = repository.findByEntityId(entityId)
                .orElseThrow(() -> new ReputationNotFoundException(entityId));

        return new ReputationResponse(
                score.getEntityId(),
                score.getEntityType(),
                score.getScore(),
                score.getLastUpdated()
        );
    }
}
