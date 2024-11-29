CREATE TABLE robot_supports
(
    "robot_support_id" uuid PRIMARY KEY,
    "robot_id" varchar NOT NULL REFERENCES "robots" ("robot_id"),
    "emergency_queue_id" uuid NOT NULL REFERENCES "emergency_queue" ("emergency_queue_id"),
    "created_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "deleted_at" timestamp DEFAULT null
);