CREATE TABLE call_reservations
(
    "call_reservation_id" uuid PRIMARY KEY,
    "doctor_id"           varchar   NOT NULL REFERENCES "doctors" ("doctor_id"),
    "user_id"             varchar   NOT NULL REFERENCES "users" ("user_id"),
    "call_start_time"     timestamp NOT NULL,
    "call_end_time"       timestamp NOT NULL,
    "created_at"          timestamp DEFAULT CURRENT_TIMESTAMP,
    "updated_at"          timestamp DEFAULT CURRENT_TIMESTAMP,
    "deleted_at"          timestamp DEFAULT null
);