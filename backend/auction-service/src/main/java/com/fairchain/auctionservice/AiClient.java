package com.fairchain.auctionservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Calls AI's GET /ai/fair-price-band per Section 4.1/2.1
 * ("Backend -> AI Services: REST calls (synchronous)").
 */
@Component
public class AiClient {

    private final RestTemplate restTemplate;
    private final String aiBaseUrl;

    public AiClient(RestTemplate restTemplate, @Value("${fairchain.ai.base-url}") String aiBaseUrl) {
        this.restTemplate = restTemplate;
        this.aiBaseUrl = aiBaseUrl;
    }

    public FairPriceBandResponse getFairPriceBand(String crop, String region) {
        String url = UriComponentsBuilder.fromHttpUrl(aiBaseUrl + "/ai/fair-price-band")
                .queryParam("crop", crop)
                .queryParam("region", region)
                .toUriString();

        return restTemplate.getForObject(url, FairPriceBandResponse.class);
    }
}
