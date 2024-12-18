package com.unicorn.api.application_service.emergency

import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.emergency.EmergencyRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface DeleteEmergencyService {
    fun deleteByUserID(userID: UserID)
}

@Service
class DeleteEmergencyServiceImpl(
    private val userRepository: UserRepository,
    private val emergencyRepository: EmergencyRepository,
) : DeleteEmergencyService {
    override fun deleteByUserID(userID: UserID) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        emergencyRepository.deleteByUser(user)
    }
}
