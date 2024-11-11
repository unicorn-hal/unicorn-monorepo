package com.unicorn.api.controller.health_checkup

import com.unicorn.api.application_service.health_checkup.SendMailHealthCheckupService
import com.unicorn.api.domain.health_checkup.HealthCheckupMailEvent
import com.unicorn.api.query_service.health_checkup.HealthCheckupQueryService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

interface HealthCheckupEventListener {
    fun onSendMail(healthCheckupMailEvent: HealthCheckupMailEvent)
}

@Component
class HealthCheckupEventListenerImpl(
    private val healthCheckupQueryService: HealthCheckupQueryService,
    private val sendMailHealthCheckupService: SendMailHealthCheckupService,
) : HealthCheckupEventListener {
    @EventListener
    override fun onSendMail(healthCheckupMailEvent: HealthCheckupMailEvent) {
        val healthCheckup = healthCheckupQueryService.getOrNullBy(healthCheckupMailEvent.HealthCheckup.healthCheckupID) ?: return
        sendMailHealthCheckupService.convertAndSend(healthCheckup)
    }
}
