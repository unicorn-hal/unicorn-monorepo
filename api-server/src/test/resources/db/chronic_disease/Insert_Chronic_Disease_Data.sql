INSERT INTO chronic_diseases (
    "chronic_disease_id",
    "user_id",
    "disease_id",
    "created_at",
    "deleted_at"
) VALUES (
    'f47ac10b-58cc-4372-a567-0e02b2c3d470',
    'test',
    1,
    NOW(),
    null
), (
    'f47ac10b-58cc-4372-a567-0e02b2c3d471',
    'test',
    2,
    NOW(),
    NOW()
);