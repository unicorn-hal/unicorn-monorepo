package com.unicorn.api.application_service.notification

import com.unicorn.api.controller.notification.NotificationPutRequest
import com.unicorn.api.domain.notification.*
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.notification.NotificationRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface UpdateNotificationService {
    fun update(
        userID: UserID,
        notificationPutRequest: NotificationPutRequest,
    ): NotificationPutRequest
}

@Service
class UpdateNotificationServiceImpl(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
) : UpdateNotificationService {
    override fun update(
        userID: UserID,
        notificationPutRequest: NotificationPutRequest,
    ): NotificationPutRequest {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val notification = notificationRepository.getOrNullBy(userID)
        requireNotNull(notification) { "Notification not found" }

        val updateNotification =
            notification.update(
                isMedicineReminder = IsMedicineReminder(notificationPutRequest.isMedicineReminder),
                isRegularHealthCheckup = IsRegularHealthCheckup(notificationPutRequest.isRegularHealthCheckup),
                isHospitalNews = IsHospitalNews(notificationPutRequest.isHospitalNews),
            )
        notificationRepository.store(updateNotification)

        return notificationPutRequest
    }
}
