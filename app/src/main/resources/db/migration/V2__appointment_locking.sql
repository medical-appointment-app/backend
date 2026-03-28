-- ── Appointment locking support ──────────────────────────────────────────────

-- Optimistic-lock version column (managed by JPA @Version).
ALTER TABLE appointments ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

-- Nullable column holding the expiry time of a LOCKED appointment.
ALTER TABLE appointments ADD COLUMN locked_until TIMESTAMP;

-- Partial unique index: only one active (non-cancelled) record per time slot.
-- Two clients racing to lock the same slot will hit this constraint; the second
-- transaction gets a unique-violation error which the service translates to HTTP 409.
CREATE UNIQUE INDEX uq_appointments_active_slot
    ON appointments (scheduled_at)
    WHERE status <> 'CANCELLED';

-- Fast lookup of expired locks for the cleanup scheduler.
CREATE INDEX idx_appointments_locked_until
    ON appointments (locked_until)
    WHERE status = 'LOCKED';
