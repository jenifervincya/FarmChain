-- Matches Section 4.3 schema exactly.
-- Only batch-service's directly-owned table is created here;
-- other tables (farmers, events, auctions, etc.) belong to
-- whichever service owns that part of the schema.

CREATE TABLE IF NOT EXISTS batches (
    id BIGSERIAL PRIMARY KEY,
    tracking_code VARCHAR(64) NOT NULL UNIQUE,
    farmer_id VARCHAR(64) NOT NULL,
    crop VARCHAR(128) NOT NULL,
    quantity_kg DOUBLE PRECISION NOT NULL,
    region VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_batches_farmer_id ON batches (farmer_id);
CREATE INDEX IF NOT EXISTS idx_batches_region_crop ON batches (region, crop);
