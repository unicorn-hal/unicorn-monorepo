package com.unicorn.api.application_service.notification

import com.unicorn.api.controller.notification.NotificationPostRequest
import com.unicorn.api.domain.notification.*
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.notification.NotificationRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import java.util.*

interface SaveNotificationService {
    fun save(
        userID: UserID,
        notificationPostRequest: NotificationPostRequest,
    ): NotificationPostRequest
}

@Service
class SaveNotificationServiceImpl(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
) : SaveNotificationService {
    override fun save(
        userID: UserID,
        notificationPostRequest: NotificationPostRequest,
    ): NotificationPostRequest {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val notification =
            Notification.create(
                userID = userID,
                isMedicineReminder = notificationPostRequest.isMedicineReminder,
                isRegularHealthCheckup = notificationPostRequest.isRegularHealthCheckup,
                isHospitalNews = notificationPostRequest.isHospitalNews,
            )

        notificationRepository.store(notification)

        return notificationPostRequest
    }
}
