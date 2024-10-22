CREATE TABLE app_config
(
    "app_config_id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    "available"     boolean NOT NULL,
    "created_at"    timestamp DEFAULT CURRENT_TIMESTAMP
);