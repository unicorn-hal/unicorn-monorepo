package com.unicorn.api.application_service.health_checkup

import com.unicorn.api.controller.health_checkup.HealthCheckupPutRequest
import com.unicorn.api.domain.health_checkup.*
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.health_checkup.HealthCheckupRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import java.util.*

interface UpdateHealthCheckupService {
    fun update(
        userID: UserID,
        healthCheckupID: HealthCheckupID,
        healthCheckupPutRequest: HealthCheckupPutRequest,
    ): HealthCheckup
}

@Service
class UpdateHealthCheckupServiceImpl(
    private val userRepository: UserRepository,
    private val healthCheckupRepository: HealthCheckupRepository,
) : UpdateHealthCheckupService {
    override fun update(
        userID: UserID,
        healthCheckupID: HealthCheckupID,
        healthCheckupPutRequest: HealthCheckupPutRequest,
    ): HealthCheckup {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val healthCheckup = healthCheckupRepository.getOrNullBy(healthCheckupID)
        requireNotNull(healthCheckup) { "Health checkup not found" }

        val updatedHealthCheckup =
            healthCheckup.update(
                bodyTemperature = BodyTemperature(healthCheckupPutRequest.bodyTemperature),
                bloodPressure = BloodPressure(healthCheckupPutRequest.bloodPressure),
                medicalRecord = MedicalRecord(healthCheckupPutRequest.medicalRecord),
                date = CheckupedDate(healthCheckupPutRequest.date),
            )

        healthCheckupRepository.store(updatedHealthCheckup)
        return updatedHealthCheckup
    }
}
