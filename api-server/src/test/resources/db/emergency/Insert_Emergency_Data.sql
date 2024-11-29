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
        1.1,
        1.1,
        NOW(),
        NOW()
    );