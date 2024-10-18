CREATE TABLE chat_support_hours
(
    "chat_support_id" uuid PRIMARY KEY,
    "doctor_id"       varchar NOT NULL REFERENCES "doctors" ("doctor_id"),
    "start_time"      time    NOT NULL,
    "end_time"        time    NOT NULL,
    "created_at"      timestamp DEFAULT CURRENT_TIMESTAMP,
    "updated_at"      timestamp DEFAULT CURRENT_TIMESTAMP,
    "deleted_at"      timestamp DEFAULT null
);