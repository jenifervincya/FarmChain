-- Matches Section 4.3's farmers table.

CREATE TABLE IF NOT EXISTS farmers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(32) NOT NULL UNIQUE,
    region VARCHAR(128) NOT NULL,
    preferred_language VARCHAR(32)
);

CREATE INDEX IF NOT EXISTS idx_farmers_region ON farmers (region);
