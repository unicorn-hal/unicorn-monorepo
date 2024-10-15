CREATE TABLE "CallSupportHours" (
  "callSupportID" uuid PRIMARY KEY,
  "doctorID" varchar NOT NULL REFERENCES "Doctors" ("doctorID"),
  "startTime" time NOT NULL,
  "endTime" time NOT NULL,
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updatedAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);