CREATE UNIQUE INDEX uniq_doctor_department_active
    ON doctor_departments (doctor_id, department_id)
    WHERE deleted_at IS NULL;