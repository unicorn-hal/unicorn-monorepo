CREATE TABLE users
(
    "user_id"      varchar PRIMARY KEY REFERENCES "accounts" ("uid"),
    "first_name"   varchar NOT NULL,
    "last_name"    varchar NOT NULL,
    "gender"       gender  NOT NULL,
    "email"        varchar NOT NULL,
    "birth_date"   date    NOT NULL,
    "address"      varchar NOT NULL,
    "postal_code"  varchar NOT NULL,
    "phone_number" varchar NOT NULL,
    "icon_image"   bytea,
    "body_height"  decimal NOT NULL,
    "body_weight"  decimal NOT NULL,
    "occupation"   varchar NOT NULL,
    "created_at"   timestamp DEFAULT CURRENT_TIMESTAMP,
    "updated_at"   timestamp DEFAULT CURRENT_TIMESTAMP,
    "deleted_at"   timestamp DEFAULT null
);