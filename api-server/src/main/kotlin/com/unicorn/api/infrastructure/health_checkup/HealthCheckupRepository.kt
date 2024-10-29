package com.unicorn.api.infrastructure.health_checkup

import com.unicorn.api.domain.user.UserID
import com.unicorn.api.domain.health_checkup.*
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

interface HealthCheckupRepository {
    fun store(healthCheckup: HealthCheckup): HealthCheckup
    fun getOrNullBy(healthCheckupID: HealthCheckupID): HealthCheckup?
    fun delete(healthCheckup: HealthCheckup)
}

@Repository
class HealthCheckupRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : HealthCheckupRepository {

    override fun store(healthCheckup: HealthCheckup): HealthCheckup {
        // laguage=postgresql
        val sql = """
            INSERT INTO health_checkups(
                health_checkup_id,
                checkuped_user_id,
                body_temperature,
                blood_pressure,
                medical_record,
                checkuped_date,
                created_at
            ) VALUES (
                :healthCheckupID,
                :userID,
                :bodyTemperature,
                :bloodPressure,
                :medicalRecord,
                :date,
                NOW()
            )
            ON CONFLICT (health_checkup_id)
            DO UPDATE SET
                body_temperature = EXCLUDED.body_temperature,
                blood_pressure = EXCLUDED.blood_pressure,
                medical_record = EXCLUDED.medical_record,
                checkuped_date = EXCLUDED.checkuped_date,
                deleted_at = NULL
            WHERE health_checkups.created_at IS NOT NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("healthCheckupID", healthCheckup.healthCheckupID.value)
            .addValue("userID", healthCheckup.userID.value)
            .addValue("bodyTemperature", healthCheckup.bodyTemperature.value)
            .addValue("bloodPressure", healthCheckup.bloodPressure.value)
            .addValue("medicalRecord", healthCheckup.medicalRecord.value)
            .addValue("date", LocalDate.parse(healthCheckup.date.value))

        namedParameterJdbcTemplate.update(sql, sqlParams)

        return healthCheckup
    }

    override fun getOrNullBy(healthCheckupID: HealthCheckupID): HealthCheckup? {
        //language=postgresql
        val sql = """
            SELECT
                health_checkup_id,
                checkuped_user_id,
                body_temperature,
                blood_pressure,
                medical_record,
                checkuped_date
            FROM health_checkups
            WHERE health_checkup_id = :healthCheckupID
            AND deleted_at IS NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("healthCheckupID", healthCheckupID.value)
        
        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams
        ) { rs, _ ->
            HealthCheckup.fromStore(
                healthCheckupID = UUID.fromString(rs.getString("health_checkup_id")),
                userID = rs.getString("checkuped_user_id"),
                bodyTemperature = rs.getDouble("body_temperature"),
                bloodPressure = rs.getString("blood_pressure"),
                medicalRecord = rs.getString("medical_record"),
                date = rs.getString("checkuped_date")
            )
        }.singleOrNull()
    }

    override fun delete(healthCheckup: HealthCheckup) {
        // language=postgresql
        val sql = """
            UPDATE health_checkups
            SET deleted_at = NOW()
            WHERE health_checkup_id = :healthCheckupID
            AND deleted_at IS NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("healthCheckupID", healthCheckup.healthCheckupID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}