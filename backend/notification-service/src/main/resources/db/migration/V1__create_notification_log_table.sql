-- NOT in Section 4.3's table list — new table for notification-service.
-- Flagged: needs adding to Section 4.3 officially per Section 6 rule #2.

CREATE TABLE IF NOT EXISTS notification_log (
    id BIGSERIAL PRIMARY KEY,
    farmer_id VARCHAR(64),
    batch_id VARCHAR(64) NOT NULL,
    event_type VARCHAR(32) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(16) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_notification_log_farmer_id ON notification_log (farmer_id);
CREATE INDEX IF NOT EXISTS idx_notification_log_status ON notification_log (status);
