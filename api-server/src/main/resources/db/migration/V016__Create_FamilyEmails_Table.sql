CREATE TABLE "FamilyEmails" (
  "familyEmailID" uuid PRIMARY KEY,
  "userID" varchar NOT NULL REFERENCES "Users" ("userID"),
  "email" varchar NOT NULL,
  "familyFirstName" varchar NOT NULL,
  "familyLastName" varchar NOT NULL,
  "phoneNumber" varchar NOT NULL,
  "iconImage" bytea,
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updatedAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);