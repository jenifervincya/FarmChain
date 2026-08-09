-- Matches Section 4.3's price_history table.

CREATE TABLE IF NOT EXISTS price_history (
    id BIGSERIAL PRIMARY KEY,
    crop VARCHAR(128) NOT NULL,
    region VARCHAR(128) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    source VARCHAR(64) NOT NULL,
    recorded_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_price_history_crop_region ON price_history (crop, region, recorded_at DESC);
