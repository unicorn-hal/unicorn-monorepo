package com.unicorn.api.query_service.notification

import com.unicorn.api.domain.user.UserID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

interface NotificationQueryService {
    fun getOrNullBy(userID: UserID): NotificationDto?
}

@Service
class NotificationQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : NotificationQueryService {
    override fun getOrNullBy(userID: UserID): NotificationDto? {
        val sql =
            """
            SELECT
                user_id,
                is_medicine_reminder,
                is_regular_health_checkup,
                is_hospital_news
            FROM user_notifications
            WHERE user_id = :userID
            """.trimIndent()

        val sqlParams = MapSqlParameterSource().addValue("userID", userID.value)
        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            NotificationDto(
                userID = rs.getString("user_id"),
                isMedicineReminder = rs.getBoolean("is_medicine_reminder"),
                isRegularHealthCheckup = rs.getBoolean("is_regular_health_checkup"),
                isHospitalNews = rs.getBoolean("is_hospital_news"),
            )
        }.singleOrNull()
    }
}

data class NotificationDto(
    val userID: String,
    val isMedicineReminder: Boolean,
    val isRegularHealthCheckup: Boolean,
    val isHospitalNews: Boolean,
)
