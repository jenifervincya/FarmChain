package com.fairchain.ledgerservice;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implements POST /api/v1/events per Section 4.1
 * ("Log a pickup/transport/warehouse/delivery event").
 */
@RestController
@RequestMapping("/api/v1/events")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> recordEvent(@Valid @RequestBody EventRequest request) {
        EventResponse response = ledgerService.recordEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
