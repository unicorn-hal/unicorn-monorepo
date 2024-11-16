INSERT INTO family_emails (
    "family_email_id",
    "user_id",
    "email",
    "family_first_name",
    "family_last_name",
    "icon_image_url",
    "created_at",
    "deleted_at"
) VALUES (
    'f47ac10b-58cc-4372-a567-0e02b2c3d470',
    'test',
    'sample@sample.com',
    '太郎',
    '山田',
    'https://example.com',
    NOW(),
    null
), (
    'f47ac10b-58cc-4372-a567-0e02b2c3d471',
    'test',
    'sample@sample.com',
    'john',
    'doe',
    'https://example.com',
    NOW(),
    NOW()
);
