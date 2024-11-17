package com.unicorn.api.infrastructure.notification

import com.unicorn.api.domain.notification.*
import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/notification/Insert_Notification_Data.sql")
class NotificationRepositoryTest {
    @Autowired
    private lateinit var notificationRepository: NotificationRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findNotificationBy(userID: UserID): Notification? {
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

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", userID.value)

        return namedParameterJdbcTemplate.queryForObject(sql, sqlParams) { rs, _ ->
            Notification.fromStore(
                userID = rs.getString("user_id"),
                isMedicineReminder = rs.getBoolean("is_medicine_reminder"),
                isRegularHealthCheckup = rs.getBoolean("is_regular_health_checkup"),
                isHospitalNews = rs.getBoolean("is_hospital_news"),
            )
        }
    }

    @Test
    fun `should store notification`() {
        val notification =
            Notification.create(
                userID = UserID("test"),
                isMedicineReminder = true,
                isRegularHealthCheckup = true,
                isHospitalNews = true,
            )

        notificationRepository.store(notification)

        val storedNotification = findNotificationBy(notification.userID)

        assertNotNull(storedNotification)
        assertEquals(notification, storedNotification)
    }

    @Test
    fun `should update notification`() {
        val notification =
            Notification.create(
                userID = UserID("test"),
                isMedicineReminder = true,
                isRegularHealthCheckup = true,
                isHospitalNews = true,
            )

        val updatedNotification =
            notification.update(
                isMedicineReminder = IsMedicineReminder(false),
                isRegularHealthCheckup = IsRegularHealthCheckup(false),
                isHospitalNews = IsHospitalNews(false),
            )

        notificationRepository.store(updatedNotification)

        val result = findNotificationBy(notification.userID)

        assertNotNull(result)
        assertEquals(updatedNotification, result)
    }

    @Test
    fun `should find notification by user id`() {
        val userID = UserID("test")
        val foundNotification = notificationRepository.getOrNullBy(userID)

        assertNotNull(foundNotification)
        assertEquals(userID.value, foundNotification!!.userID.value)
        assertEquals(true, foundNotification.isMedicineReminder.value)
        assertEquals(true, foundNotification.isRegularHealthCheckup.value)
        assertEquals(true, foundNotification.isHospitalNews.value)
    }

    @Test
    fun `should not find notification by user id`() {
        val userID = UserID("test2")
        val foundNotification = notificationRepository.getOrNullBy(userID)

        assertNull(foundNotification)
    }
}
