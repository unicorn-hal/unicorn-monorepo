CREATE TABLE messages
(
    "message_id" uuid PRIMARY KEY,
    "chat_id"    uuid    NOT NULL REFERENCES "chats" ("chat_id"),
    "sender_id"  varchar NOT NULL REFERENCES "accounts" ("uid"),
    "content"    varchar NOT NULL,
    "created_at" timestamp DEFAULT CURRENT_TIMESTAMP,
    "deleted_at" timestamp DEFAULT null
);