CREATE SCHEMA IF NOT EXISTS contact_management;

CREATE TABLE IF NOT EXISTS contact_management.contact (
    contact_id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(255),
    mobile_number VARCHAR(11),
    phone_number VARCHAR(10),
    is_favorite CHAR(1),
    is_active CHAR(1),
    created_at TIMESTAMP WITHOUT TIME ZONE
);
