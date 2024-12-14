package com.unicorn.api.controller.emergency

import com.unicorn.api.application_service.emergency.SendMailEmergencyService
import com.unicorn.api.domain.emergency.RobotDispatchedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

interface DispatchedEventListener {
    fun onSendMail(robotDispatchedEvent: RobotDispatchedEvent)
}

@Component
class DispatchedEventListenerImpl(
    private val sendMailEmergencyService: SendMailEmergencyService,
) : DispatchedEventListener {
    @EventListener
    override fun onSendMail(robotDispatchedEvent: RobotDispatchedEvent) {
        sendMailEmergencyService.send(robotDispatchedEvent.emergency)
    }
}
