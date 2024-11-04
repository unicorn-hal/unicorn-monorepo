package com.unicorn.api.query_service.health_checkup

import com.unicorn.api.domain.health_checkup.HealthCheckupID
import com.unicorn.api.domain.user.UserID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

interface HealthCheckupQueryService {
    fun getBy(userID: UserID): HealthCheckupResult

    fun getOrNullBy(healthcheckupID: HealthCheckupID): HealthCheckupDto?
}

@Service
class HealthCheckupQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : HealthCheckupQueryService {
    override fun getBy(userID: UserID): HealthCheckupResult {
        // language=postgresql
        val sql =
            """
            SELECT
                health_checkup_id,
                checkuped_user_id,
                body_temperature,
                blood_pressure,
                medical_record,
                checkuped_date
            FROM health_checkups
            WHERE checkuped_user_id = :userID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", userID.value)
        val healthCheckups =
            namedParameterJdbcTemplate.query(
                sql,
                sqlParams,
            ) { rs, _ ->
                HealthCheckupDto(
                    healthCheckupID = UUID.fromString(rs.getString("health_checkup_id")),
                    userID = rs.getString("checkuped_user_id"),
                    bodyTemperature = rs.getDouble("body_temperature"),
                    bloodPressure = rs.getString("blood_pressure"),
                    medicalRecord = rs.getString("medical_record"),
                    date = rs.getDate("checkuped_date").toLocalDate(),
                )
            }
        return HealthCheckupResult(healthCheckups)
    }

    override fun getOrNullBy(healthcheckupID: HealthCheckupID): HealthCheckupDto? {
        // language=postgresql
        val sql =
            """
            SELECT
                health_checkup_id,
                checkuped_user_id,
                body_temperature,
                blood_pressure,
                medical_record,
                checkuped_date
            FROM health_checkups
            WHERE deleted_at IS NULL 
            AND health_checkup_id = :health_checkup_id
            """.trimIndent()

        val params =
            MapSqlParameterSource()
                .addValue("health_checkup_id", healthcheckupID.value)

        return namedParameterJdbcTemplate.query(sql, params) { rs, _ ->
            HealthCheckupDto(
                healthCheckupID = UUID.fromString(rs.getString("health_checkup_id")),
                userID = rs.getString("checkuped_user_id"),
                bodyTemperature = rs.getDouble("body_temperature"),
                bloodPressure = rs.getString("blood_pressure"),
                medicalRecord = rs.getString("medical_record"),
                date = rs.getDate("checkuped_date").toLocalDate(),
            )
        }.singleOrNull()
    }
}

data class HealthCheckupDto(
    val healthCheckupID: UUID,
    val userID: String,
    val bodyTemperature: Double,
    val bloodPressure: String,
    val medicalRecord: String,
    val date: LocalDate,
)

data class HealthCheckupResult(
    val data: List<HealthCheckupDto>,
)
