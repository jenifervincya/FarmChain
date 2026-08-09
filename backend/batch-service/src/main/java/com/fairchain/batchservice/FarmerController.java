package com.fairchain.batchservice;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * NOT in Section 4.1's endpoint table — added because batches
 * reference farmer_id and no service currently creates/reads
 * farmer records. Flagged to team; path may need to be agreed
 * on and added to Section 4.1 officially.
 */
@RestController
@RequestMapping("/api/v1/farmers")
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @PostMapping
    public ResponseEntity<FarmerResponse> registerFarmer(@Valid @RequestBody FarmerRequest request) {
        FarmerResponse response = farmerService.registerFarmer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{farmerId}")
    public ResponseEntity<FarmerResponse> getFarmer(@PathVariable String farmerId) {
        FarmerResponse response = farmerService.getFarmer(farmerId);
        return ResponseEntity.ok(response);
    }
}
