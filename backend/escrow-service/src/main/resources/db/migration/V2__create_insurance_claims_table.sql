-- Matches Section 4.3's insurance_claims table.

CREATE TABLE IF NOT EXISTS insurance_claims (
    id BIGSERIAL PRIMARY KEY,
    batch_id VARCHAR(64) NOT NULL,
    expected_price DOUBLE PRECISION NOT NULL,
    actual_price DOUBLE PRECISION NOT NULL,
    payout_amount DOUBLE PRECISION,
    triggered_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_insurance_claims_batch_id ON insurance_claims (batch_id);
