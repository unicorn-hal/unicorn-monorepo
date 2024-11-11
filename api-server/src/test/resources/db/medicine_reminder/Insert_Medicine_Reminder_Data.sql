INSERT INTO medicine_reminders (reminder_id, medicine_id, reminder_time, day_of_week, created_at) VALUES
('123e4567-e89b-12d3-a456-426614174010', '123e4567-e89b-12d3-a456-426614174000', '08:00', '{monday, tuesday}', NOW()),
('123e4567-e89b-12d3-a456-426614174011', '123e4567-e89b-12d3-a456-426614174000', '12:00', '{wednesday, thursday}', NOW()),
('123e4567-e89b-12d3-a456-426614174012', '123e4567-e89b-12d3-a456-426614174001', '16:00', '{friday, saturday}', NOW()),
('123e4567-e89b-12d3-a456-426614174013', '123e4567-e89b-12d3-a456-426614174001', '20:00', '{sunday}', NOW());