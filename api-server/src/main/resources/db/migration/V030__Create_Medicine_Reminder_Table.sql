CREATE TABLE medicine_reminders (
   reminder_id UUID PRIMARY KEY,
   medicine_id UUID NOT NULL REFERENCES Medicines(medicine_id),
   reminder_time TIME NOT NULL,
   day_of_week VARCHAR[] NOT NULL,
   created_at TIMESTAMP NOT NULL,
   deleted_at TIMESTAMP
);
