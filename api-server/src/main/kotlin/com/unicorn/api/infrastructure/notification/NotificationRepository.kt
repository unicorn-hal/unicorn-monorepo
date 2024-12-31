package com.unicorn.api.infrastructure.notification

import com.unicorn.api.domain.notification.*
import com.unicorn.api.domain.user.User
import com.unicorn.api.domain.user.UserID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface NotificationRepository {
    fun store(notification: Notification): Notification

    fun getOrNullBy(userID: UserID): Notification?

    fun deleteByUser(user: User)
}

@Repository
class NotificationRepositoryImpl(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : NotificationRepository {
    override fun store(notification: Notification): Notification {
        // language=postgresql
        val sql =
            """
            INSERT INTO user_notifications (
                user_id,
                is_medicine_reminder,
                is_regular_health_checkup,
                is_hospital_news
            ) VALUES (
                :userID,
                :isMedicineReminder,
                :isRegularHealthCheckup,
                :isHospitalNews
            )
            ON CONFLICT (user_id)
            DO UPDATE SET
                is_medicine_reminder = :isMedicineReminder,
                is_regular_health_checkup = :isRegularHealthCheckup,
                is_hospital_news = :isHospitalNews
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", notification.userID.value)
                .addValue("isMedicineReminder", notification.isMedicineReminder.value)
                .addValue("isRegularHealthCheckup", notification.isRegularHealthCheckup.value)
                .addValue("isHospitalNews", notification.isHospitalNews.value)

        jdbcTemplate.update(sql, sqlParams)

        return notification
    }

    override fun getOrNullBy(userID: UserID): Notification? {
        // language=postgresql
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

        return jdbcTemplate.query(sql, sqlParams) { rs, _ ->
            Notification.fromStore(
                userID = rs.getString("user_id"),
                isMedicineReminder = rs.getBoolean("is_medicine_reminder"),
                isRegularHealthCheckup = rs.getBoolean("is_regular_health_checkup"),
                isHospitalNews = rs.getBoolean("is_hospital_news"),
            )
        }.singleOrNull()
    }

    override fun deleteByUser(user: User) {
        // language=postgresql
        val sql =
            """
            UPDATE user_notifications
            SET 
                is_medicine_reminder = FALSE,
                is_regular_health_checkup = FALSE,
                is_hospital_news = FALSE
            WHERE user_id = :userID;
            """.trimIndent()

        val sqlParams = MapSqlParameterSource().addValue("userID", user.userID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
