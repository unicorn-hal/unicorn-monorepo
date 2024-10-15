CREATE TABLE "Users" (
  "userID" varchar PRIMARY KEY REFERENCES "Accounts" ("uid"),
  "firstName" varchar NOT NULL,
  "lastName" varchar NOT NULL,
  "gender" Gender NOT NULL,
  "birthDate" date NOT NULL,
  "address" varchar NOT NULL,
  "postalCode" varchar NOT NULL,
  "phoneNumber" varchar NOT NULL,
  "iconImage" bytea,
  "bodyHeight" decimal NOT NULL,
  "bodyWeight" decimal NOT NULL,
  "occupation" varchar NOT NULL,
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updatedAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);