package com.fairchain.batchservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class BatchService {

    private final BatchRepository batchRepository;
    private final KafkaTemplate<String, BatchRegisteredEvent> kafkaTemplate;
    private final String batchRegisteredTopic;

    public BatchService(BatchRepository batchRepository,
                         KafkaTemplate<String, BatchRegisteredEvent> kafkaTemplate,
                         @Value("${fairchain.kafka.topic.batch-registered}") String batchRegisteredTopic) {
        this.batchRepository = batchRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.batchRegisteredTopic = batchRegisteredTopic;
    }

    public BatchResponse registerBatch(BatchRequest request) {
        String trackingCode = generateTrackingCode(request.getCrop());
        Instant now = Instant.now();

        Batch batch = new Batch(
                trackingCode,
                request.getFarmerId(),
                request.getCrop(),
                request.getQuantityKg(),
                request.getRegion(),
                "REGISTERED",
                now
        );

        Batch saved = batchRepository.save(batch);

        BatchRegisteredEvent event = new BatchRegisteredEvent(
                saved.getId().toString(),
                saved.getFarmerId(),
                saved.getCrop(),
                saved.getRegion(),
                saved.getQuantityKg(),
                now
        );
        kafkaTemplate.send(batchRegisteredTopic, saved.getId().toString(), event);

        return new BatchResponse(
                saved.getId().toString(),
                saved.getTrackingCode(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }

    public BatchDetailResponse getBatchByTrackingCode(String trackingCode) {
        Batch batch = batchRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() -> new BatchNotFoundException(trackingCode));

        return new BatchDetailResponse(
                batch.getId().toString(),
                batch.getTrackingCode(),
                batch.getFarmerId(),
                batch.getCrop(),
                batch.getQuantityKg(),
                batch.getRegion(),
                batch.getStatus(),
                batch.getCreatedAt()
        );
    }

    public PriceJourneyResponse getPriceJourney(String trackingCode) {
        Batch batch = batchRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() -> new BatchNotFoundException(trackingCode));

        return new PriceJourneyResponse(
                batch.getId().toString(),
                batch.getTrackingCode(),
                batch.getCrop(),
                batch.getRegion(),
                batch.getStatus(),
                batch.getCreatedAt()
        );
    }

    private String generateTrackingCode(String crop) {
        String cropCode = crop.length() >= 3
                ? crop.substring(0, 3).toUpperCase()
                : crop.toUpperCase();
        String suffix = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return "FC-" + cropCode + "-" + suffix;
    }
}
