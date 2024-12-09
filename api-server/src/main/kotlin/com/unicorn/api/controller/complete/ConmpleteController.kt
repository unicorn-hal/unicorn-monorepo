package com.unicorn.api.controller.complete

import com.unicorn.api.application_service.complete.NotifyCompleteService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.robot.RobotID
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
class CompleteController(
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val notifyCompleteService: NotifyCompleteService,
) {
    @PostMapping("/unicorn/{robotID}/complete")
    fun post(
        @RequestHeader("X-UID") uid: String,
        @PathVariable robotID: String,
        @RequestBody completePostRequest: CompletePostRequest,
    ): ResponseEntity<*> {
        try {
            val result = notifyCompleteService.notify(RobotID(robotID), completePostRequest)
            simpMessagingTemplate.convertAndSend("/topic/unicorn/users/${completePostRequest.userID}", result)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}

data class CompletePostRequest(
    val robotSupportID: UUID,
    val userID: String,
)
