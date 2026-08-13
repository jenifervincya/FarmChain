package com.fairchain.reputationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ReputationService {

    private final ReputationScoreRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final double DEFAULT_SCORE = 100.0;
    private static final double PENALTY_PER_DEVIATION = 5.0;

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

    @KafkaListener(topics = "${fairchain.kafka.topic.price-deviation}", groupId = "reputation-service")
    public void onPriceDeviation(String rawPayload) throws Exception {
        PriceDeviationEvent event = objectMapper.readValue(rawPayload, PriceDeviationEvent.class);

        if (event.getNodeChain() == null || event.getNodeChain().length == 0) {
            return;
        }
        String entityId = event.getNodeChain()[event.getNodeChain().length - 1];

        ReputationScore score = repository.findByEntityId(entityId)
                .orElseGet(() -> new ReputationScore(entityId, "buyer", DEFAULT_SCORE, Instant.now()));

        double newScore = Math.max(0.0, score.getScore() - PENALTY_PER_DEVIATION);
        score.updateScore(newScore, Instant.now());
        repository.save(score);
    }
}
