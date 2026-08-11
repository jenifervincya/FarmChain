package com.fairchain.auctionservice;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> findByBatchId(String batchId);
}
