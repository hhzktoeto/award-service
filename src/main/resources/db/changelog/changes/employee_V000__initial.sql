CREATE TABLE employees
(
    id         BIGINT PRIMARY KEY,
    full_name  VARCHAR(255) NOT NULL,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp
)