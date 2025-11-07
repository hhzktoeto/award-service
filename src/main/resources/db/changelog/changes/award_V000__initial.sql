CREATE TABLE awards
(
    id          BIGINT PRIMARY KEY,
    employee_id BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    date        DATE         NOT NULL,
    created_at  timestamptz DEFAULT current_timestamp,
    updated_at  timestamptz DEFAULT current_timestamp,

    CONSTRAINT fk_award_employee_id FOREIGN KEY (employee_id) REFERENCES employees (id)
)