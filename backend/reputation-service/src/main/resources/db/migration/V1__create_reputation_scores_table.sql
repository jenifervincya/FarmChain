-- Matches Section 4.3's reputation_scores table.

CREATE TABLE IF NOT EXISTS reputation_scores (
    id BIGSERIAL PRIMARY KEY,
    entity_id VARCHAR(64) NOT NULL UNIQUE,
    entity_type VARCHAR(32) NOT NULL,
    score DOUBLE PRECISION NOT NULL,
    last_updated TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_reputation_entity_type ON reputation_scores (entity_type);
