-- Matches Section 4.3's transactions table.

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    batch_id VARCHAR(64) NOT NULL UNIQUE,
    buyer_id VARCHAR(64) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    escrow_status VARCHAR(16) NOT NULL,
    released_at TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_transactions_status ON transactions (escrow_status);
