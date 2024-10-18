CREATE TABLE medicines
(
    "medicine_id"   uuid PRIMARY KEY,
    "user_id"       varchar REFERENCES "users" ("user_id"),
    "count"         integer NOT NULL,
    "quantity"      integer NOT NULL,
    "medicine_name" varchar NOT NULL,
    "created_at"    timestamp DEFAULT CURRENT_TIMESTAMP,
    "deleted_at"    timestamp DEFAULT null
);