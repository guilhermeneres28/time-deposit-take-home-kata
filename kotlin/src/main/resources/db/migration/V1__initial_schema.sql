CREATE TABLE time_deposits (
    id BIGSERIAL PRIMARY KEY,
    plan_type VARCHAR(50) NOT NULL,
    balance DECIMAL(19, 4) NOT NULL,
    days INTEGER NOT NULL
);

CREATE TABLE withdrawals (
    id BIGSERIAL PRIMARY KEY,
    time_deposit_id BIGINT NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    date DATE NOT NULL,
    CONSTRAINT fk_time_deposit FOREIGN KEY (time_deposit_id) REFERENCES time_deposits(id) ON DELETE CASCADE
);

CREATE INDEX idx_time_deposits_plan_type ON time_deposits(plan_type);
CREATE INDEX idx_withdrawals_time_deposit_id ON withdrawals(time_deposit_id);

INSERT INTO time_deposits (plan_type, balance, days) VALUES
    ('BASIC', 1000.0000, 31),
    ('STUDENT', 2000.0000, 61),
    ('PREMIUM', 5000.0000, 76),
    ('BASIC', 10000.0000, 15),
    ('STUDENT', 3000.0000, 365),
    ('PREMIUM', 7500.0000, 46);
