package com.unicorn.api.infrastructure.doctor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.unicorn.api.domain.doctor.*
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface DoctorRepository {
    fun store(doctor: Doctor): Doctor
    fun getOrNullBy(doctorID: DoctorID): Doctor?
    fun delete(doctorID: DoctorID): Unit
}

@Repository
class DoctorRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : DoctorRepository {
    // language=postgresql
    private val insertSql = """
        INSERT INTO doctors (
            doctor_id,
            hospital_id,
            email,
            phone_number,
            first_name,
            last_name,
            doctor_icon_url,
            created_at
        ) VALUES (
            :doctorID,
            :hospitalID,
            :email,
            :phoneNumber,
            :firstName,
            :lastName,
            :doctorIconUrl,
            NOW()
        )
        ON CONFLICT (doctor_id) DO UPDATE
        SET
            hospital_id = EXCLUDED.hospital_id,
            first_name = EXCLUDED.first_name,
            last_name = EXCLUDED.last_name,
            email = EXCLUDED.email,
            phone_number = EXCLUDED.phone_number,
            doctor_icon_url = EXCLUDED.doctor_icon_url,
            deleted_at = NULL
        WHERE doctors.created_at IS NOT NULL
    """.trimIndent()

    // language=postgresql
    private val deleteRelationSql = """
        UPDATE doctor_departments
        SET deleted_at = NOW()
        WHERE doctor_id = :doctorID
        AND deleted_at IS NULL
    """.trimIndent()

    // language=postgresql
    private val insertRelationSql = """
        INSERT INTO doctor_departments (
            doctor_department_id,
            doctor_id,
            department_id,
            created_at
        ) VALUES (
            :id,
            :doctorID,
            :departmentID,
            NOW()
        )
        ON CONFLICT (doctor_id, department_id) WHERE deleted_at IS NULL DO UPDATE
        SET
            deleted_at = NULL
        WHERE doctor_departments.created_at IS NOT NULL
    """.trimIndent()

    // language=postgresql
    private val selectSql = """
        SELECT
            doctors.doctor_id,
            hospital_id,
            email,
            phone_number,
            first_name,
            last_name,
            doctor_icon_url,
            JSONB_AGG(dd.department_id) as departments
        FROM doctors
        LEFT JOIN doctor_departments dd on doctors.doctor_id = dd.doctor_id
        WHERE doctors.doctor_id = :doctorID
            AND doctors.deleted_at IS NULL
            AND dd.deleted_at IS NULL
        GROUP BY doctors.doctor_id
    """.trimIndent()

    // language=postgresql
    private val deleteSql = """
        UPDATE doctors
        SET deleted_at = NOW()
        WHERE doctor_id = :doctorID
        AND deleted_at IS NULL
    """.trimIndent()

    override fun store(doctor: Doctor): Doctor {
        val doctorSqlParams = MapSqlParameterSource()
            .addValue("doctorID", doctor.doctorID.value)
            .addValue("hospitalID", doctor.hospitalID.value)
            .addValue("email", doctor.email.value)
            .addValue("firstName", doctor.firstName.value)
            .addValue("phoneNumber", doctor.phoneNumber.value)
            .addValue("lastName", doctor.lastName.value)
            .addValue("doctorIconUrl", doctor.doctorIconUrl?.value)

        namedParameterJdbcTemplate.update(insertSql, doctorSqlParams)

        // 既存の診療科との関係を削除
        val deleteRelationParams = MapSqlParameterSource()
            .addValue("doctorID", doctor.doctorID.value)

        namedParameterJdbcTemplate.update(deleteRelationSql, deleteRelationParams)

        // 新しい診療科との関係を追加
        doctor.departments.forEach { departmentID ->
            val insertRelationParams = MapSqlParameterSource()
                .addValue("id", UUID.randomUUID())
                .addValue("doctorID", doctor.doctorID.value)
                .addValue("departmentID", departmentID.value)

            namedParameterJdbcTemplate.update(insertRelationSql, insertRelationParams)
        }

        return doctor
    }

    override fun getOrNullBy(doctorID: DoctorID): Doctor? {
        val sqlParams = MapSqlParameterSource()
            .addValue("doctorID", doctorID.value)

        return namedParameterJdbcTemplate.query(selectSql, sqlParams) { rs, _ ->

            Doctor.fromStore(
                doctorID = rs.getString("doctor_id"),
                hospitalID = UUID.fromString(rs.getString("hospital_id")),
                email = rs.getString("email"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                phoneNumber = rs.getString("phone_number"),
                doctorIconUrl = rs.getString("doctor_icon_url"),
                departments =  jacksonObjectMapper().readValue<List<UUID>>(rs.getString("departments"))
            )
        }.singleOrNull()
    }

    override fun delete(doctorID: DoctorID) {
        val sqlParams = MapSqlParameterSource()
            .addValue("doctorID", doctorID.value)

        namedParameterJdbcTemplate.update(deleteSql, sqlParams)

        // 関連する診療科との関係を削除
        val deleteRelationParams = MapSqlParameterSource()
            .addValue("doctorID", doctorID.value)

        namedParameterJdbcTemplate.update(deleteRelationSql, deleteRelationParams)
    }
}