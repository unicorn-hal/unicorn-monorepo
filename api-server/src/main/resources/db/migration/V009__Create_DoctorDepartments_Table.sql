CREATE TABLE doctor_departments
(
    "doctor_department_id" uuid PRIMARY KEY,
    "department_id"        uuid    NOT NULL REFERENCES "departments" ("department_id"),
    "doctor_id"            varchar NOT NULL REFERENCES "doctors" ("doctor_id"),
    "created_at"           timestamp DEFAULT CURRENT_TIMESTAMP,
    "deleted_at"           timestamp DEFAULT null
);