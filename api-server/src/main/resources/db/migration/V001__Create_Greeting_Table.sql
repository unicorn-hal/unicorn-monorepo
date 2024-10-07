Create TABLE greeting (
    id UUID PRIMARY KEY,
    message VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

INSERT INTO greeting (
    id,
    message
) VALUES (
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    'Hello, World!'
)
