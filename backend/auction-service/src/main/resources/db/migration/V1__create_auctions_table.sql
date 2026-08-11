CREATE TABLE IF NOT EXISTS auctions (
    id BIGSERIAL PRIMARY KEY,
    batch_id VARCHAR(64) NOT NULL UNIQUE,
    status VARCHAR(16) NOT NULL,
    floor_price DOUBLE PRECISION NOT NULL,
    winning_bid DOUBLE PRECISION,
    winning_buyer_id VARCHAR(64)
);

CREATE INDEX IF NOT EXISTS idx_auctions_status ON auctions (status);
