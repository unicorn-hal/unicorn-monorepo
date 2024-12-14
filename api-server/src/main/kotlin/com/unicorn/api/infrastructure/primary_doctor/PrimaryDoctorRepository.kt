package com.unicorn.api.infrastructure.primary_doctor

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.primary_doctor.*
import com.unicorn.api.domain.user.UserID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface PrimaryDoctorRepository {
    fun store(primaryDoctor: PrimaryDoctor): PrimaryDoctor

    fun getOrNullBy(primaryDoctorID: PrimaryDoctorID): PrimaryDoctor?

    fun getOrNullByUserID(userID: UserID): List<PrimaryDoctor>

    fun getOrNullByDoctorIDAndUserID(
        doctorID: DoctorID,
        userID: UserID,
    ): PrimaryDoctor?

    fun delete(primaryDoctor: PrimaryDoctor)
}

@Repository
class PrimaryDoctorRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : PrimaryDoctorRepository {
    override fun store(primaryDoctor: PrimaryDoctor): PrimaryDoctor {
        // language=postgresql
        val sql =
            """
            INSERT INTO primary_doctors (
                primary_doctor_id,
                user_id,
                doctor_id,
                created_at
            ) VALUES (
                :primaryDoctorID,
                :userID,
                :doctorID,
                NOW()
            )
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("primaryDoctorID", primaryDoctor.primaryDoctorID.value)
                .addValue("userID", primaryDoctor.userID.value)
                .addValue("doctorID", primaryDoctor.doctorID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)

        return primaryDoctor
    }

    override fun getOrNullBy(primaryDoctorID: PrimaryDoctorID): PrimaryDoctor? {
        // language=postgresql
        val sql =
            """
            SELECT
                primary_doctor_id,
                user_id,
                doctor_id,
                created_at
            FROM primary_doctors
            WHERE primary_doctor_id = :primaryDoctorID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("primaryDoctorID", primaryDoctorID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            PrimaryDoctor.fromStore(
                primaryDoctorID = UUID.fromString(rs.getString("primary_doctor_id")),
                userID = rs.getString("user_id"),
                doctorID = rs.getString("doctor_id"),
            )
        }.singleOrNull()
    }

    override fun getOrNullByUserID(userID: UserID): List<PrimaryDoctor> {
        // language=postgresql
        val sql =
            """
            SELECT
                primary_doctor_id,
                user_id,
                doctor_id,
                created_at
            FROM primary_doctors
            WHERE user_id = :userID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", userID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            PrimaryDoctor.fromStore(
                primaryDoctorID = UUID.fromString(rs.getString("primary_doctor_id")),
                userID = rs.getString("user_id"),
                doctorID = rs.getString("doctor_id"),
            )
        }
    }

    override fun getOrNullByDoctorIDAndUserID(
        doctorID: DoctorID,
        userID: UserID,
    ): PrimaryDoctor? {
        // language=postgresql
        val sql =
            """
            SELECT
                primary_doctor_id,
                user_id,
                doctor_id
            FROM primary_doctors
            WHERE user_id = :userID
            AND doctor_id = :doctorID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", userID.value)
                .addValue("doctorID", doctorID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            PrimaryDoctor.fromStore(
                primaryDoctorID = UUID.fromString(rs.getString("primary_doctor_id")),
                userID = rs.getString("user_id"),
                doctorID = rs.getString("doctor_id"),
            )
        }.singleOrNull()
    }

    override fun delete(primaryDoctor: PrimaryDoctor) {
        // language=postgresql
        val sql =
            """
            UPDATE primary_doctors
            SET deleted_at = NOW()
            WHERE primary_doctor_id = :primaryDoctorID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("primaryDoctorID", primaryDoctor.primaryDoctorID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
