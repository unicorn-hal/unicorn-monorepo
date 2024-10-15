CREATE TABLE "Accounts" (
  "uid" varchar PRIMARY KEY,
  "fcmTokenID" varchar NOT NULL,
  "role" Role NOT NULL,
  "deletedAt" timestamp DEFAULT null
);