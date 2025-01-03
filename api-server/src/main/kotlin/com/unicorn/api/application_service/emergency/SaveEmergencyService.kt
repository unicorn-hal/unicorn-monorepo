package com.unicorn.api.application_service.emergency

import com.unicorn.api.controller.emergency.EmergencyPostRequest
import com.unicorn.api.domain.emergency.Emergency
import com.unicorn.api.domain.emergency.EmergencySavedEvent
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.emergency.EmergencyRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.util.*

interface SaveEmergencyService {
    fun save(emergencyPostRequest: EmergencyPostRequest): Emergency
}

@Service
class SaveEmergencyServiceImpl(
    private val emergencyRepository: EmergencyRepository,
    private val userRepository: UserRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : SaveEmergencyService {
    override fun save(emergencyPostRequest: EmergencyPostRequest): Emergency {
        val user = userRepository.getOrNullBy(UserID(emergencyPostRequest.userID))
        requireNotNull(user) { "User not found" }

        val emergency =
            Emergency.create(
                userID = emergencyPostRequest.userID,
                userLatitude = emergencyPostRequest.userLatitude,
                userLongitude = emergencyPostRequest.userLongitude,
            )

        emergencyRepository.store(emergency)
        val waitingEmergencyList = emergencyRepository.getOlderOrNull()
        for (waitingEmergency in waitingEmergencyList) {
            applicationEventPublisher.publishEvent(EmergencySavedEvent(waitingEmergency))
        }
        return emergency
    }
}
