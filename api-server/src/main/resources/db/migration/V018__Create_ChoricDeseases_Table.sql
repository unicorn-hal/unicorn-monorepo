CREATE TABLE "ChronicDiseases" (
  "diseaseID" uuid PRIMARY KEY,
  "userID" varchar NOT NULL REFERENCES "Users" ("userID"),
  "diseaseName" varchar NOT NULL,
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);