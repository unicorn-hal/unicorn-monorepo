CREATE TABLE emergency_queue
(
    "emergency_queue_id" uuid PRIMARY KEY,
    "user_id" varchar NOT NULL REFERENCES "users" ("user_id"),
    "user_latitude" decimal NOT NULL,
    "user_longitude" decimal NOT NULL,
    "created_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "deleted_at" timestamp DEFAULT null
)