INSERT INTO robot_supports (
    robot_support_id,
    robot_id,
    emergency_queue_id,
    created_at,
    deleted_at
) VALUES (
    'c64e788c-dd0a-72d8-a271-5460e1f29816',
    'robot',
    'f47ac10b-58cc-4372-a567-0e02b2c3d470',
    NOW(),
    null
), (
    'c64e788c-dd0a-72d8-a271-5460e1f29817',
    'robot',
    'f47ac10b-58cc-4372-a567-0e02b2c3d471',
    NOW(),
    NOW()
);