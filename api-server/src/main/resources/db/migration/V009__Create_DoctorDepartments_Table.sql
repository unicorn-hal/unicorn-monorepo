CREATE TABLE "DoctorDepartments" (
  "doctorDepartmentID" uuid PRIMARY KEY,
  "departemntID" uuid NOT NULL,
  "doctorID" varchar NOT NULL REFERENCES "Doctors" ("doctorID"),
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);