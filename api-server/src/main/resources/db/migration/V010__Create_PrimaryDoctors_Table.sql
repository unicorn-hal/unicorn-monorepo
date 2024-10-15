CREATE TABLE "PrimaryDoctors" (
  "primaryDoctorID" uuid PRIMARY KEY,
  "doctorID" varchar NOT NULL REFERENCES "Doctors" ("doctorID"),
  "userID" varchar NOT NULL REFERENCES "Users" ("userID"),
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);