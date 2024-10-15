CREATE TABLE "Hospitals" (
  "hospitalID" uuid PRIMARY KEY,
  "hospitalName" varchar NOT NULL,
  "postalCode" varchar NOT NULL,
  "phoneNumber" varchar NOT NULL,
  "deletedAt" timestamp DEFAULT null
);