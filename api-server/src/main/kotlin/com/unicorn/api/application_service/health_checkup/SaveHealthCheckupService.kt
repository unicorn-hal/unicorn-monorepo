package com.unicorn.api.application_service.health_checkup

import com.unicorn.api.controller.health_checkup.HealthCheckupPostRequest
import com.unicorn.api.domain.health_checkup.HealthCheckup
import com.unicorn.api.domain.health_checkup.HealthCheckupMailEvent
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.health_checkup.HealthCheckupRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.util.*

interface SaveHealthCheckupService {
    fun save(
        userID: UserID,
        healthCheckupPostRequest: HealthCheckupPostRequest,
    ): HealthCheckup
}

@Service
class SaveHealthCheckupServiceImpl(
    private val userRepository: UserRepository,
    private val healthCheckupRepository: HealthCheckupRepository,
    private val eventPublisher: ApplicationEventPublisher,
) : SaveHealthCheckupService {
    override fun save(
        userID: UserID,
        healthCheckupPostRequest: HealthCheckupPostRequest,
    ): HealthCheckup {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val healthCheckup =
            HealthCheckup.create(
                userID = userID.value,
                bodyTemperature = healthCheckupPostRequest.bodyTemperature,
                bloodPressure = healthCheckupPostRequest.bloodPressure,
                medicalRecord = healthCheckupPostRequest.medicalRecord,
                date = healthCheckupPostRequest.date,
            )

        healthCheckupRepository.store(healthCheckup)
        eventPublisher.publishEvent(HealthCheckupMailEvent(healthCheckup))
        return healthCheckup
    }
}
