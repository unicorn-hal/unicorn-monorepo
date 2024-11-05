package com.unicorn.api.application_service.health_checkup

import com.unicorn.api.domain.health_checkup.HealthCheckupID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.health_checkup.HealthCheckupRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface DeleteHealthCheckupService {
    fun delete(
        userID: UserID,
        healthCheckupID: HealthCheckupID,
    ): Unit
}

@Service
class DeleteHealthCheckupServiceImpl(
    private val userRepository: UserRepository,
    private val healthCheckupRepository: HealthCheckupRepository,
) : DeleteHealthCheckupService {
    override fun delete(
        userID: UserID,
        healthCheckupID: HealthCheckupID,
    ) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val healthCheckup = healthCheckupRepository.getOrNullBy(healthCheckupID)
        requireNotNull(healthCheckup) { "Health checkup not found" }

        healthCheckupRepository.delete(healthCheckup)
    }
}
