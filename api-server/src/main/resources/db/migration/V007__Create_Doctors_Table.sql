CREATE TABLE "Doctors" (
  "doctorID" varchar PRIMARY KEY REFERENCES "Accounts" ("uid"),
  "hospitalID" uuid NOT NULL REFERENCES "Hospitals" ("hospitalID"),
  "firstName" varchar NOT NULL,
  "lastName" varchar NOT NULL,
  "doctorIcon" bytea,
  "email" varchar NOT NULL,
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updatedAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);