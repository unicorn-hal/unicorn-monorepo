CREATE TABLE health_checkups
(
    "health_checkup_id" uuid PRIMARY KEY,
    "checkuped_user_id" varchar NOT NULL REFERENCES "users" ("user_id"),
    "body_temperature"  decimal NOT NULL,
    "blood_pressure"    varchar NOT NULL,
    "medical_record"    text    NOT NULL,
    "created_at"        timestamp DEFAULT CURRENT_TIMESTAMP,
    "updated_at"        timestamp DEFAULT CURRENT_TIMESTAMP,
    "deleted_at"        timestamp DEFAULT null
);