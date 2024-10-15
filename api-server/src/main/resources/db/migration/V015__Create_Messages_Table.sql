CREATE TABLE "Messages" (
  "messageID" uuid PRIMARY KEY,
  "chatID" uuid NOT NULL REFERENCES "Chats" ("chatID"),
  "senderID" varchar NOT NULL REFERENCES "Accounts" ("uid"),
  "content" varchar NOT NULL,
  "createdAt" timestamp DEFAULT CURRENT_TIMESTAMP,
  "deletedAt" timestamp DEFAULT null
);