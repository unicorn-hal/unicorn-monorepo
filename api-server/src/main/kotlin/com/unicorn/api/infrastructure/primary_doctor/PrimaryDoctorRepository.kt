package com.unicorn.api.infrastructure.primary_doctor

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.primary_doctor.PrimaryDoctor
import com.unicorn.api.domain.primary_doctor.PrimaryDoctorID
import com.unicorn.api.domain.primary_doctor.PrimaryDoctors
import com.unicorn.api.domain.user.UserID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface PrimaryDoctorRepository {
    fun store(primaryDoctors: PrimaryDoctors): PrimaryDoctors

    fun getOrNullBy(
        userID: UserID,
        primaryDoctorID: PrimaryDoctorID,
    ): PrimaryDoctors?

    fun getOrNullByUserID(userID: UserID): PrimaryDoctors?
}

@Repository
class PrimaryDoctorRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : PrimaryDoctorRepository {
    override fun store(primaryDoctors: PrimaryDoctors): PrimaryDoctors {
        val deleteSql =
            """
            UPDATE primary_doctors
            SET deleted_at = CURRENT_TIMESTAMP
            WHERE user_id = :userId  AND deleted_at IS NULL
            """.trimIndent()

        val deleteParams =
            MapSqlParameterSource()
                .addValue("userId", primaryDoctors.userID.value)
        namedParameterJdbcTemplate.update(deleteSql, deleteParams)

        primaryDoctors.doctors.forEach { primaryDoctor ->
            // language=postgresql
            val insertSql =
                """
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
                ) ON CONFLICT (primary_doctor_id) DO UPDATE
                SET
                    doctor_id = EXCLUDED.doctor_id,
                    user_id = EXCLUDED.user_id,
                    created_at = CURRENT_TIMESTAMP,
                    deleted_at = NULL
                """.trimIndent()

            val insertParams =
                MapSqlParameterSource()
                    .addValue("primaryDoctorId", primaryDoctor.primaryDoctorID.value)
                    .addValue("doctorId", primaryDoctor.doctorID.value)
                    .addValue("userId", primaryDoctors.userID.value)
            namedParameterJdbcTemplate.update(insertSql, insertParams)
        }

        return primaryDoctors
    }

    override fun getOrNullBy(
        userID: UserID,
        primaryDoctorID: PrimaryDoctorID,
    ): PrimaryDoctors? {
        // language=postgresql
        val sql =
            """
            SELECT primary_doctor_id, doctor_id, user_id
            FROM primary_doctors
            WHERE primary_doctor_id = :primaryDoctorId
            AND user_id = :userId
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("primaryDoctorId", primaryDoctorID.value)
                .addValue("userId", userID.value)

        val doctors =
            namedParameterJdbcTemplate.query(
                sql,
                sqlParams,
            ) { rs, _ ->
                PrimaryDoctor.fromStore(
                    primaryDoctorID = PrimaryDoctorID(UUID.fromString(rs.getString("primary_doctor_id"))),
                    doctorID = DoctorID(rs.getString("doctor_id")),
                )
            }

        return if (doctors.isNotEmpty()) {
            PrimaryDoctors.fromExistingDoctor(userID, doctors)
        } else {
            null
        }
    }

    override fun getOrNullByUserID(userID: UserID): PrimaryDoctors? {
        // language=postgresql
        val sql =
            """
            SELECT primary_doctor_id, doctor_id, user_id
            FROM primary_doctors
            WHERE user_id = :userId
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userId", userID.value)

        val doctors =
            namedParameterJdbcTemplate.query(
                sql,
                sqlParams,
            ) { rs, _ ->
                PrimaryDoctor.fromStore(
                    primaryDoctorID = PrimaryDoctorID(UUID.fromString(rs.getString("primary_doctor_id"))),
                    doctorID = DoctorID(rs.getString("doctor_id")),
                )
            }

        return doctors.takeIf { it.isNotEmpty() }?.let { PrimaryDoctors.fromExistingDoctor(userID, it) }
    }
}
