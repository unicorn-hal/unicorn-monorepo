package com.unicorn.api.infrastructure.primary_doctor

import com.unicorn.api.domain.primary_doctor.PrimaryDoctor
import com.unicorn.api.domain.primary_doctor.PrimaryDoctorID
import com.unicorn.api.domain.primary_doctor.PrimaryDoctors
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface PrimaryDoctorRepository {
    fun store(primaryDoctors: PrimaryDoctors): PrimaryDoctors
    fun getOrNullBy(userID: UserID, primaryDoctorID: PrimaryDoctorID): PrimaryDoctors?
}

@Repository
class PrimaryDoctorRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : PrimaryDoctorRepository {

    override fun store(primaryDoctors: PrimaryDoctors): PrimaryDoctors {
        val currentDoctorIDs = primaryDoctors.doctors.map { it.doctorID }
        val (commonDoctors, doctorsToAdd, doctorsToDelete) = primaryDoctors.updateDoctors(currentDoctorIDs)

        doctorsToDelete.forEach { primaryDoctor ->
            // language=postgresql
            val deleteSql = """
        UPDATE primary_doctors
        SET deleted_at = CURRENT_TIMESTAMP
        WHERE user_id = :userId AND doctor_id = :doctorId AND deleted_at IS NULL
    """.trimIndent()

            val deleteParams = MapSqlParameterSource()
                .addValue("userId", primaryDoctors.userID.value)
                .addValue("doctorId", primaryDoctor.doctorID.value)
            namedParameterJdbcTemplate.update(deleteSql, deleteParams)
        }

        doctorsToAdd.forEach { primaryDoctor ->
            // language=postgresql
            val insertSql = """
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
        """.trimIndent()

            val insertParams = MapSqlParameterSource()
                .addValue("primaryDoctorId", primaryDoctor.primaryDoctorID.value)
                .addValue("doctorId", primaryDoctor.doctorID.value)
                .addValue("userId", primaryDoctors.userID.value)
            namedParameterJdbcTemplate.update(insertSql, insertParams)
        }

        // 更新された PrimaryDoctors インスタンスを作成して返す
        val updatedDoctors = commonDoctors + doctorsToAdd
        return PrimaryDoctors.fromExistingDoctor(primaryDoctors.userID, updatedDoctors)
    }

    override fun getOrNullBy(userID: UserID, primaryDoctorID: PrimaryDoctorID): PrimaryDoctors? {
        // language=postgresql
        val sql = """
            SELECT primary_doctor_id, doctor_id, user_id
            FROM primary_doctors
            WHERE primary_doctor_id = :primaryDoctorId
            AND user_id = :userId
            AND deleted_at IS NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("primaryDoctorId", primaryDoctorID.value)
            .addValue("userId", userID.value)

        val doctors = namedParameterJdbcTemplate.query(
            sql,
            sqlParams
        ) { rs, _ ->
            PrimaryDoctor.fromStore(
                primaryDoctorID = PrimaryDoctorID(UUID.fromString(rs.getString("primary_doctor_id"))),
                doctorID = DoctorID(rs.getString("doctor_id"))
            )
        }

        return if (doctors.isNotEmpty()) {
            PrimaryDoctors.fromExistingDoctor(userID, doctors)
        } else null
    }
}
