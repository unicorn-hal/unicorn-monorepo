INSERT INTO doctor_departments (
    doctor_department_id,
    doctor_id,
    department_id,
    created_at,
    deleted_at
) VALUES (
    gen_random_uuid(),
    'doctor',
    'b68a87a3-b7f1-4b85-b0ab-6c620d68d791',
    NOW(),
    NULL
),(
   gen_random_uuid(),
    'doctor',
    'a1dcb69e-472f-4a57-90a2-f2c63b62ec90',
    NOW(),
    NOW()
);