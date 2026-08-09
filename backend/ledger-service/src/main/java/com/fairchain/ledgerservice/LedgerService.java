package com.fairchain.ledgerservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class LedgerService {

    private final LedgerEventRepository repository;
    private final KafkaTemplate<String, LedgerEventPayload> kafkaTemplate;
    private final Map<String, String> eventTypeToTopic;

    public LedgerService(LedgerEventRepository repository,
                          KafkaTemplate<String, LedgerEventPayload> kafkaTemplate,
                          @Value("${fairchain.kafka.topic.pickup-event}") String pickupTopic,
                          @Value("${fairchain.kafka.topic.transport-event}") String transportTopic,
                          @Value("${fairchain.kafka.topic.warehouse-event}") String warehouseTopic,
                          @Value("${fairchain.kafka.topic.delivery-event}") String deliveryTopic) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.eventTypeToTopic = Map.of(
                "pickup", pickupTopic,
                "transport", transportTopic,
                "warehouse", warehouseTopic,
                "delivery", deliveryTopic
        );
    }

    public EventResponse recordEvent(EventRequest request) {
        String eventType = request.getEventType().toLowerCase();
        String topic = eventTypeToTopic.get(eventType);
        if (topic == null) {
            throw new InvalidEventTypeException(request.getEventType());
        }

        String previousHash = repository
                .findFirstByBatchIdOrderByTimestampDesc(request.getBatchId())
                .map(LedgerEvent::getHash)
                .orElse(null); // null = genesis event for this batch

        Instant now = Instant.now();

        String hash = HashChainUtil.computeHash(
                request.getBatchId(),
                eventType,
                request.getLocation(),
                now,
                request.getMetadataJson(),
                previousHash
        );

        LedgerEvent event = new LedgerEvent(
                request.getBatchId(),
                eventType,
                request.getLocation(),
                now,
                request.getMetadataJson(),
                hash,
                previousHash
        );
        LedgerEvent saved = repository.save(event);

        LedgerEventPayload payload = new LedgerEventPayload(
                request.getBatchId(),
                now,
                request.getLocation(),
                "delivery".equals(eventType) ? request.getConfirmedBy() : null
        );
        kafkaTemplate.send(topic, request.getBatchId(), payload);

        return new EventResponse(
                saved.getId().toString(),
                saved.getBatchId(),
                saved.getEventType(),
                saved.getTimestamp(),
                saved.getHash()
        );
    }
}
