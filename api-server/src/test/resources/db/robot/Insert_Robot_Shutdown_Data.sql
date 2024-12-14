INSERT INTO robots (
        robot_id,
        name,
        status,
        created_at,
        deleted_at
    ) VALUES (
        'test',
        'robotName',
        'shutdown',
        NOW(),
        null
    ),(
        '12345',
        'robotName2',
        'shutdown',
        NOW(),
        NOW()
    ),(
        'testtest',
        'robotName3',
        'shutdown',
        NOW(),
        null
    );