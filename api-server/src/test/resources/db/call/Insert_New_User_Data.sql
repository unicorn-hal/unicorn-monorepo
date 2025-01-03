INSERT INTO users
(user_id,
 first_name,
 last_name,
 gender,
 email,
 birth_date,
 address,
 postal_code,
 phone_number,
 icon_image_url,
 body_height,
 body_weight,
 occupation,
 created_at,
 deleted_at)
VALUES ('test',
        'test',
        'test',
        'male',
        'sample@test.com',
        '1990-01-01',
        'test',
        '0000000',
        '00000000000',
        'https://example.com',
        170.4,
        60.4,
        'test',
        '2024-10-20 13:17:04.322379',
        NULL),
        ('12345',
        '田中',
        '太郎',
        'female',
        'tanaka@test.com',
        '2000-01-01',
        'test',
        '0000000',
        '00000000000',
        'https://example.com',
        165.2,
        50.1,
        'test',
        NOW(),
        NULL),
        ('testtest',
        '財前',
        '五郎',
        'male',
        'zaizen@test.com',
        '2000-01-01',
        'test',
        '0000000',
        '00000000000',
        'https://example.com',
        185.2,
        90.1,
        'test',
        NOW(),
        NULL),
        ('deleted_test_user',
        '削除',
        '太郎',
        'male',
        'delete@test.com',
        '2001-01-17',
        '千葉県市川市',
        '2720811',
        '0471111111',
        'https://example.com',
        175.4,
        60.1,
        'test',
        NOW(),
        NOW());