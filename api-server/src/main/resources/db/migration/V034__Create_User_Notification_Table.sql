CREATE TABLE user_notifications
(
    "user_id" varchar NOT NULL PRIMARY KEY REFERENCES "users" ("user_id"),
    "is_medicine_reminder" boolean NOT NULL,
    "is_regular_health_checkup" boolean NOT NULL,
    "is_hospital_news" boolean NOT NULL
);