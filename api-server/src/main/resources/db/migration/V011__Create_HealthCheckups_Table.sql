CREATE TABLE "HealthCheckups" (
  "healthCheckupID" uuid PRIMARY KEY,
  "checkupedUserID" varchar NOT NULL REFERENCES "Users" ("userID"),
  "bodyTemperature" decimal NOT NULL,
  "bloodPressure" varchar NOT NULL,
  "medicalRecord" text NOT NULL,
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updatedAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);