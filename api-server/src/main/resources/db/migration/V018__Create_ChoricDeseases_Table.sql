CREATE TABLE chronic_diseases
(
    "disease_id"   uuid PRIMARY KEY,
    "user_id"      varchar NOT NULL REFERENCES "users" ("user_id"),
    "disease_name" varchar NOT NULL,
    "created_at"   timestamp DEFAULT CURRENT_TIMESTAMP,
    "deleted_at"   timestamp DEFAULT null
);