INSERT INTO emergency_queue (
    emergency_queue_id,
    user_id,
    user_latitude,
    user_longitude,
    created_at,
    deleted_at
    ) VALUES (
        'f47ac10b-58cc-4372-a567-0e02b2c3d470',
        'test',
        1.1,
        1.1,
        NOW(),
        null
    ), (
        'f47ac10b-58cc-4372-a567-0e02b2c3d471',
        'test',
        1.2,
        1.2,
        NOW() + INTERVAL '1 day',
        NOW()
    ), (
        'f47ac10b-58cc-4372-a567-0e02b2c3d469',
        'test',
        1.3,
        1.3,
        NOW() + INTERVAL '2 day',
        null
    );