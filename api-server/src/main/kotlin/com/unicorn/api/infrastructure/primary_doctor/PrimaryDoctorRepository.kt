package com.unicorn.api.infrastructure.primary_doctor

import com.unicorn.api.domain.primary_doctor.PrimaryDoctor
import com.unicorn.api.domain.primary_doctor.PrimaryDoctorID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface PrimaryDoctorRepository {
    fun store(primaryDoctors: List<PrimaryDoctor>): List<PrimaryDoctor?>
    fun getOrNullBy(primaryDoctorID: PrimaryDoctorID): PrimaryDoctor?
}

@Repository
class PrimaryDoctorRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : PrimaryDoctorRepository {
    override fun store(primaryDoctors: List<PrimaryDoctor>): List<PrimaryDoctor?> {
        // language=postgresql
        val sql = """
        INSERT INTO primary_doctors (
            primary_doctor_id,
            doctor_id,
            user_id,
            created_at,
            deleted_at
        ) VALUES (
            :primaryDoctorId,
            :doctorId,
            :userId,
            CURRENT_TIMESTAMP,
            NULL
        )
        ON CONFLICT (primary_doctor_id) DO UPDATE SET
            doctor_id = EXCLUDED.doctor_id,
            user_id = EXCLUDED.user_id,
            deleted_at = NULL,
            created_at = CURRENT_TIMESTAMP
        RETURNING primary_doctor_id, doctor_id, user_id
    """.trimIndent()

        val updatedPrimaryDoctors = primaryDoctors.flatMap { primaryDoctor ->
            primaryDoctor.primaryDoctors.map { primaryDoctorEntry ->
                val params = MapSqlParameterSource()
                    .addValue("primaryDoctorId", primaryDoctorEntry.primaryDoctorID.value)
                    .addValue("doctorId", primaryDoctorEntry.doctorID.value)
                    .addValue("userId", primaryDoctorEntry.userID.value)

                namedParameterJdbcTemplate.query(
                    sql,
                    params
                ) { rs, _ ->
                    PrimaryDoctor.fromStore(
                        primaryDoctorID = rs.getObject("primary_doctor_id", UUID::class.java),
                        userID = rs.getString("user_id"),
                        doctorID = rs.getString("doctor_id")
                    )
                }.singleOrNull()
            }
        }
        return updatedPrimaryDoctors
    }

    override fun getOrNullBy(primaryDoctorID: PrimaryDoctorID): PrimaryDoctor? {
        // language=postgresql
        val sql = """
        SELECT primary_doctor_id, doctor_id, user_id
        FROM primary_doctors
        WHERE primary_doctor_id = :primaryDoctorId
        AND deleted_at IS NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource().addValue("primaryDoctorId", primaryDoctorID.value)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams
        ) { rs, _ ->
            PrimaryDoctor.fromStore(
                primaryDoctorID = rs.getObject("primary_doctor_id", UUID::class.java),
                userID = rs.getString("user_id"),
                doctorID = rs.getString("doctor_id")
            )
        }.singleOrNull()
    }
}