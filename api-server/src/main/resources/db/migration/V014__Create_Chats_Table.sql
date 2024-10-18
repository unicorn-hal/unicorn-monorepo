CREATE TABLE chats
(
    "chat_id"    uuid PRIMARY KEY,
    "doctor_id"  varchar NOT NULL REFERENCES "doctors" ("doctor_id"),
    "user_id"    varchar NOT NULL REFERENCES "users" ("user_id"),
    "created_at" timestamp DEFAULT CURRENT_TIMESTAMP,
    "deleted_at" timestamp DEFAULT null
);