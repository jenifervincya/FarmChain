package com.fairchain.auctionservice;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("/{batchId}/bids")
    public ResponseEntity<BidResponse> placeBid(@PathVariable String batchId,
                                                 @Valid @RequestBody BidRequest request) {
        BidResponse response = auctionService.placeBid(batchId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{batchId}/status")
    public ResponseEntity<AuctionStatusResponse> getStatus(@PathVariable String batchId) {
        AuctionStatusResponse response = auctionService.getStatus(batchId);
        return ResponseEntity.ok(response);
    }
}
