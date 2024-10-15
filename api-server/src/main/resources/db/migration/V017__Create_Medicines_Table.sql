CREATE TABLE "Medicines" (
  "medicineID" uuid PRIMARY KEY,
  "userID" varchar REFERENCES "Users" ("userID"),
  "count" integer NOT NULL,
  "quantity" integer NOT NULL,
  "medicineName" varchar NOT NULL,
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);
-- updatedAt も欲しいかも