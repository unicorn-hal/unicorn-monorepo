-- 検索用の医師データ
INSERT INTO doctors (doctor_id, hospital_id, first_name, last_name, phone_number, doctor_icon_url, email, created_at)
VALUES ('doctor2',
        '762a7a7e-41e4-46c2-b36c-f2b302cae3e7',
        '田中',
        '太郎',
        '1234567890',
        'https://example.com',
        'test@test.com',
        NOW());

INSERT INTO doctor_departments (doctor_department_id,
                                doctor_id,
                                department_id,
                                created_at,
                                deleted_at)
VALUES (gen_random_uuid(),
        'doctor2',
        'b68a87a3-b7f1-4b85-b0ab-6c620d68d791',
        NOW(),
        NULL);

INSERT INTO chat_support_hours (chat_support_id,
                                doctor_id,
                                start_time,
                                end_time,
                                created_at)
VALUES ('d1b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b3a',
        'doctor2',
        '9:00:00',
        '18:00:00',
        NOW());

INSERT INTO call_support_hours (call_support_id,
                                doctor_id,
                                start_time,
                                end_time,
                                created_at)
VALUES ('d1b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b2c',
        'doctor2',
        '9:00:00',
        '18:00:00',
        NOW());

