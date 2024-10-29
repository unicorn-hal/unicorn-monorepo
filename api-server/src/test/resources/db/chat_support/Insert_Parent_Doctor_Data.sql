INSERT INTO accounts (uid, role, fcm_token_id) VALUES ('doctor2', 'doctor', 'fcm_token_id');

INSERT INTO doctors (doctor_id, hospital_id, first_name, last_name, phone_number, doctor_icon_url, email, created_at)
VALUES ('doctor2',
        'd8bfa31d-54b9-4c64-a499-6c522517e5f7',
        'test',
        'test',
        '1234567890',
        'https://example.com',
        'test@test.com',
        NOW());