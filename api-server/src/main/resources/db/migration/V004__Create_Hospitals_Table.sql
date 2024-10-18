CREATE TABLE hospitals (
  "hospital_id" uuid PRIMARY KEY,
  "hospital_name" varchar NOT NULL,
  "postal_code" varchar NOT NULL,
  "address" varchar NOT NULL,
  "phone_number" varchar NOT NULL,
  "deleted_at" timestamp DEFAULT null
);