CREATE TABLE doctors (
  "doctor_id" varchar PRIMARY KEY REFERENCES "accounts" ("uid"),
  "hospital_id" uuid NOT NULL REFERENCES "hospitals" ("hospital_id"),
  "first_name" varchar NOT NULL,
  "last_name" varchar NOT NULL,
  "doctor_icon" bytea,
  "email" varchar NOT NULL,
  "created_at" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deleted_at" timestamp DEFAULT null
);