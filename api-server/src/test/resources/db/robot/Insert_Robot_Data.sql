INSERT INTO robots (
        robot_id,
        name,
        status,
        created_at,
        deleted_at
    ) VALUES (
        'test',
        'robotName',
        'robot_waiting',
        NOW(),
        null
    ),(
        '12345',
        'robotName2',
        'robot_waiting',
        NOW(),
        NOW()
    ),(
        'testtest',
        'robotName3',
        'supporting',
        NOW(),
        null
    );