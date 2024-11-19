INSERT INTO call_reservations (
    call_reservation_id, doctor_id, user_id, call_start_time, call_end_time
) VALUES (
    '211177ed-92f8-a956-825f-c31b2cad8b15', 'doctor', 'test', '2021-01-01T09:00:00+09:00', '2021-01-01T10:30:00+09:00'
),
(
    '551177ed-92f8-a956-825f-c31b2cad8b10', 'doctor', 'test', CURRENT_TIMESTAMP + INTERVAL '30 minutes', CURRENT_TIMESTAMP + INTERVAL '59 minutes'
),
(
    '221177ed-92f8-a956-825f-c31b2cad8b10', 'doctor2', 'test', CURRENT_TIMESTAMP + INTERVAL '2 month', CURRENT_TIMESTAMP + INTERVAL '2 month 30 minutes'
),
(
    '221177ed-92f8-a956-825f-c31b2cad8b22', 'doctor2', 'test', CURRENT_TIMESTAMP + INTERVAL '9 month', CURRENT_TIMESTAMP + INTERVAL '9 month 30 minutes'
),
(
    '311177ed-92f8-a956-825f-c31b2cad8b15', 'doctor2', '12345', CURRENT_TIMESTAMP + INTERVAL '1 month', CURRENT_TIMESTAMP + INTERVAL '1 month 30 minutes'
),
(
    '661177ed-92f8-a956-825f-c31b2cad8b15', 'doctor2', '12345', CURRENT_TIMESTAMP + INTERVAL '5 year', CURRENT_TIMESTAMP + INTERVAL '5 year 30 minutes'
);