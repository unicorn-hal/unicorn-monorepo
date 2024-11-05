INSERT INTO health_checkups (
    "health_checkup_id",
    "checkuped_user_id",
    "body_temperature",
    "blood_pressure",
    "medical_record",
    "checkuped_date",
    "created_at",
    "deleted_at"
) VALUES (
    'f47ac10b-58cc-4372-a567-0e02b2c3d470',
    'test',
    36.5,
    '120/80',
    'sample medical record',
    '2020-01-01',
    NOW(),
    null
), (
    'f47ac10b-58cc-4372-a567-0e02b2c3d471',
    'test',
    36.5,
    '120/80',
    'sample medical record',
    '2020-01-01',
    NOW(),
    NOW()
);