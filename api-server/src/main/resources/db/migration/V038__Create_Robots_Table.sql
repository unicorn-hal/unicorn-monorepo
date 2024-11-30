CREATE TABLE robots (
    "robot_id" varchar PRIMARY KEY REFERENCES "accounts" ("uid"),
    "name" varchar NOT NULL,
    "status" robot_status NOT NULL,
    "created_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "deleted_at" timestamp DEFAULT null
)