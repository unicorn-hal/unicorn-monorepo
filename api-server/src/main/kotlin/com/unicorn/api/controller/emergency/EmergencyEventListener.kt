package com.unicorn.api.controller.emergency

import com.unicorn.api.application_service.emergency.RobotRequestService
import com.unicorn.api.domain.emergency.EmergencySavedEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

interface EmergencyEventListener {
    fun onEmergencyRequest(emergencySavedEvent: EmergencySavedEvent)
}

@Component
class EmergencyEventListenerImpl(
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val robotRequestService: RobotRequestService,
) : EmergencyEventListener {
    @EventListener
    override fun onEmergencyRequest(emergencySavedEvent: EmergencySavedEvent) {
        val (userStatus, robotStatus) = robotRequestService.request(emergencySavedEvent.emergency)
        if (userStatus == null) return
        simpMessagingTemplate.convertAndSend("/topic/unicorn/users/${emergencySavedEvent.emergency.userID.value}", userStatus)
        if (robotStatus == null) return
        if (userStatus.robotID == null) return
        simpMessagingTemplate.convertAndSend("/topic/unicorn/robots/${userStatus.robotID.value}", robotStatus)
    }
}
