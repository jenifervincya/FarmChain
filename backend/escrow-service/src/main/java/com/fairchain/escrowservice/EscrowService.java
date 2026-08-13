package com.fairchain.escrowservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Map;

@Service
public class EscrowService {

    private final TransactionRepository transactionRepository;
    private final InsuranceClaimRepository insuranceClaimRepository;
    private final KafkaTemplate<String, PaymentReleasedEvent> kafkaTemplate;
    private final String paymentReleasedTopic;
    private final RestTemplate restTemplate;
    private final String aiBaseUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EscrowService(TransactionRepository transactionRepository,
                          InsuranceClaimRepository insuranceClaimRepository,
                          KafkaTemplate<String, PaymentReleasedEvent> kafkaTemplate,
                          @Value("${fairchain.kafka.topic.payment-released}") String paymentReleasedTopic,
                          RestTemplate restTemplate,
                          @Value("${fairchain.ai.base-url}") String aiBaseUrl) {
        this.transactionRepository = transactionRepository;
        this.insuranceClaimRepository = insuranceClaimRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.paymentReleasedTopic = paymentReleasedTopic;
        this.restTemplate = restTemplate;
        this.aiBaseUrl = aiBaseUrl;
    }

    @KafkaListener(topics = "${fairchain.kafka.topic.sale-confirmed}", groupId = "escrow-service")
    public void onSaleConfirmed(String rawPayload) throws Exception {
        SaleConfirmedEvent event = objectMapper.readValue(rawPayload, SaleConfirmedEvent.class);

        Transaction transaction = new Transaction(
                event.getBatchId(),
                event.getFarmerId(),
                event.getBuyerId(),
                event.getPrice(),
                "PENDING",
                null
        );
        transactionRepository.save(transaction);

        checkInsurancePool(event);
    }

    @SuppressWarnings("unchecked")
    private void checkInsurancePool(SaleConfirmedEvent event) {
        String url = UriComponentsBuilder.fromHttpUrl(aiBaseUrl + "/ai/fair-price-band")
                .queryParam("crop", event.getCrop())
                .queryParam("region", event.getRegion())
                .toUriString();

        Map<String, Object> band = restTemplate.getForObject(url, Map.class);
        if (band == null || band.get("minPrice") == null) {
            return;
        }

        double minPrice = ((Number) band.get("minPrice")).doubleValue();

        if (event.getPrice() < minPrice) {
            double payout = minPrice - event.getPrice();
            InsuranceClaim claim = new InsuranceClaim(
                    event.getBatchId(),
                    minPrice,
                    event.getPrice(),
                    payout,
                    Instant.now()
            );
            insuranceClaimRepository.save(claim);
        }
    }

    @KafkaListener(topics = "${fairchain.kafka.topic.delivery-event}", groupId = "escrow-service")
    public void onDeliveryEvent(String rawPayload) throws Exception {
        DeliveryEventPayload event = objectMapper.readValue(rawPayload, DeliveryEventPayload.class);

        transactionRepository.findByBatchId(event.getBatchId()).ifPresent(transaction -> {
            if ("PENDING".equals(transaction.getEscrowStatus())) {
                Instant now = Instant.now();
                transaction.release(now);
                transactionRepository.save(transaction);

                PaymentReleasedEvent released = new PaymentReleasedEvent(
                        transaction.getBatchId(),
                        transaction.getFarmerId(),
                        transaction.getBuyerId(),
                        transaction.getAmount(),
                        now
                );
                kafkaTemplate.send(paymentReleasedTopic, transaction.getBatchId(), released);
            }
        });
    }
}
