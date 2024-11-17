package com.unicorn.api.domain.notification

import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NotificationTest {
    @Test
    fun `should create notification`() {
        val notification =
            Notification.create(
                userID = UserID("test"),
                isMedicineReminder = true,
                isRegularHealthCheckup = true,
                isHospitalNews = true,
            )

        assertEquals("test", notification.userID.value)
        assertEquals(true, notification.isMedicineReminder.value)
        assertEquals(true, notification.isRegularHealthCheckup.value)
        assertEquals(true, notification.isHospitalNews.value)
    }

    @Test
    fun `should create notification from store`() {
        val notification =
            Notification.fromStore(
                userID = "test",
                isMedicineReminder = true,
                isRegularHealthCheckup = true,
                isHospitalNews = true,
            )

        assertEquals("test", notification.userID.value)
        assertEquals(true, notification.isMedicineReminder.value)
        assertEquals(true, notification.isRegularHealthCheckup.value)
        assertEquals(true, notification.isHospitalNews.value)
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

        assertEquals("test", updatedNotification.userID.value)
        assertEquals(false, updatedNotification.isMedicineReminder.value)
        assertEquals(false, updatedNotification.isRegularHealthCheckup.value)
        assertEquals(false, updatedNotification.isHospitalNews.value)
    }
}
