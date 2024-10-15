CREATE TABLE "DoctorDepartments" (
  "doctorDepartmentID" uuid PRIMARY KEY,
  "departmentID" uuid NOT NULL REFERENCES "Departments" ("departmentID"),
  "doctorID" varchar NOT NULL REFERENCES "Doctors" ("doctorID"),
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);