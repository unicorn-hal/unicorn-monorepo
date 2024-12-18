package com.unicorn.api.application_service.notification

import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.notification.NotificationRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface DeleteNotificationService {
    fun deleteByUserID(userID: UserID)
}

@Service
class DeleteNotificationServiceImpl(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
) : DeleteNotificationService {
    override fun deleteByUserID(userID: UserID) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val notification = notificationRepository.getOrNullBy(userID)
        requireNotNull(notification) { "Notification not found" }

        notificationRepository.deleteByUser(user)
    }
}
