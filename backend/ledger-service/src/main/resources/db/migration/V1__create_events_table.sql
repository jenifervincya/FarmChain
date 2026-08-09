-- Matches Section 4.3's events table, plus hash/previous_hash columns
-- needed for hash-chaining per Section 1.3 (flagged to team — not in
-- the original Section 4.3 table as written).

CREATE TABLE IF NOT EXISTS events (
    id BIGSERIAL PRIMARY KEY,
    batch_id VARCHAR(64) NOT NULL,
    event_type VARCHAR(32) NOT NULL,
    location VARCHAR(255) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    metadata_json TEXT,
    hash VARCHAR(64) NOT NULL UNIQUE,
    previous_hash VARCHAR(64)
);

CREATE INDEX IF NOT EXISTS idx_events_batch_id ON events (batch_id);
CREATE INDEX IF NOT EXISTS idx_events_batch_timestamp ON events (batch_id, timestamp);
