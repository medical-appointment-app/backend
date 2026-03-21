-- ── Users ──────────────────────────────────────────────────────────────────
CREATE TABLE users (
    id              BIGSERIAL       PRIMARY KEY,
    external_id     VARCHAR(255)    UNIQUE,
    first_name      VARCHAR(255)    NOT NULL,
    last_name       VARCHAR(255)    NOT NULL,
    email           VARCHAR(255)    NOT NULL UNIQUE,
    phone_number    VARCHAR(50),
    date_of_birth   DATE,
    address         VARCHAR(500),
    blood_type      VARCHAR(5),
    created_at      TIMESTAMP       NOT NULL,
    updated_at      TIMESTAMP
);

-- ── Doctors ─────────────────────────────────────────────────────────────────
CREATE TABLE doctors (
    id                      BIGSERIAL       PRIMARY KEY,
    first_name              VARCHAR(255)    NOT NULL,
    last_name               VARCHAR(255)    NOT NULL,
    email                   VARCHAR(255)    NOT NULL UNIQUE,
    phone_number            VARCHAR(50),
    slot_duration_minutes   INTEGER,
    created_at              TIMESTAMP       NOT NULL,
    updated_at              TIMESTAMP
);

-- ── Admins ──────────────────────────────────────────────────────────────────
CREATE TABLE admins (
    id              BIGSERIAL       PRIMARY KEY,
    first_name      VARCHAR(255)    NOT NULL,
    last_name       VARCHAR(255)    NOT NULL,
    email           VARCHAR(255)    NOT NULL UNIQUE,
    phone_number    VARCHAR(50),
    access_level    VARCHAR(20)     NOT NULL,
    created_at      TIMESTAMP       NOT NULL,
    updated_at      TIMESTAMP
);

-- ── Appointments ────────────────────────────────────────────────────────────
CREATE TABLE appointments (
    id                  BIGSERIAL       PRIMARY KEY,
    patient_user_id     VARCHAR(255)    NOT NULL,
    doctor_id           VARCHAR(255)    NOT NULL,
    scheduled_at        TIMESTAMP       NOT NULL,
    status              VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    notes               VARCHAR(1000),
    duration_minutes    INTEGER,
    created_at          TIMESTAMP       NOT NULL
);

CREATE INDEX idx_appointments_patient    ON appointments (patient_user_id);
CREATE INDEX idx_appointments_scheduled  ON appointments (scheduled_at);
CREATE INDEX idx_appointments_status     ON appointments (status);

-- ── Catalog items ────────────────────────────────────────────────────────────
CREATE TABLE catalog_items (
    id          BIGSERIAL           PRIMARY KEY,
    name        VARCHAR(255)        NOT NULL,
    description TEXT,
    price       NUMERIC(10, 2)      NOT NULL,
    category    VARCHAR(255)        NOT NULL,
    available   BOOLEAN             NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_catalog_category  ON catalog_items (category);
CREATE INDEX idx_catalog_available ON catalog_items (available);

-- ── Content pages ────────────────────────────────────────────────────────────
CREATE TABLE content_pages (
    id      BIGSERIAL       PRIMARY KEY,
    slug    VARCHAR(255)    NOT NULL,
    title   VARCHAR(255)    NOT NULL,
    body    TEXT            NOT NULL,
    locale  VARCHAR(10)     NOT NULL,
    CONSTRAINT uq_content_slug_locale UNIQUE (slug, locale)
);

CREATE INDEX idx_content_locale ON content_pages (locale);
