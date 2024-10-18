CREATE TABLE accounts (
  "uid" varchar PRIMARY KEY,
  "fcm_token_id" varchar NOT NULL,
  "role" role NOT NULL,
  "deletedAt" timestamp DEFAULT null
);