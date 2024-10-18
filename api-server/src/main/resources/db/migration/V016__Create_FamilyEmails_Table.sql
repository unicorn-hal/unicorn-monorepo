CREATE TABLE family_emails
(
    "family_email_id"   uuid PRIMARY KEY,
    "user_id"           varchar NOT NULL REFERENCES "users" ("user_id"),
    "email"             varchar NOT NULL,
    "family_first_name" varchar NOT NULL,
    "family_last_name"  varchar NOT NULL,
    "phone_number"      varchar NOT NULL,
    "icon_image"        bytea,
    "created_at"        timestamp DEFAULT CURRENT_TIMESTAMP,
    "updated_at"        timestamp DEFAULT CURRENT_TIMESTAMP,
    "deleted_at"        timestamp DEFAULT null
);