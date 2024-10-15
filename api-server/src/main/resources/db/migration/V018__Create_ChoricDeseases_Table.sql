CREATE TABLE "ChoricDeseases" (
  "deseaseID" uuid PRIMARY KEY,
  "userID" varchar NOT NULL REFERENCES "Users" ("userID"),
  "deseaseName" varchar NOT NULL,
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);