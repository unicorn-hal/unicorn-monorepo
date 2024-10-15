CREATE TABLE "CallReservations" (
  "callReservationID" uuid PRIMARY KEY,
  "doctorID" varchar NOT NULL REFERENCES "Doctors" ("doctorID"),
  "userID" varchar NOT NULL REFERENCES "Users" ("userID"),
  "callStartTime" timestamp NOT NULL,
  "callEndTime" timestamp NOT NULL,
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updatedAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);