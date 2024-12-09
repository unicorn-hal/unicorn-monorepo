package com.unicorn.api.controller.moving

import com.unicorn.api.application_service.moving.NotifyMovingService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.robot.RobotID
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
class MovingController(
    private val notifyMovingService: NotifyMovingService,
    private val simpMessagingTemplate: SimpMessagingTemplate,
) {
    @PostMapping("/unicorn/{robotID}/moving")
    fun post(
        @RequestHeader("X-UID") uid: String,
        @PathVariable robotID: String,
        @RequestBody movingPostRequest: MovingPostRequest,
    ): ResponseEntity<*> {
        try {
            val result = notifyMovingService.notify(RobotID(robotID), movingPostRequest)
            simpMessagingTemplate.convertAndSend("/topic/unicorn/users/${movingPostRequest.userID}", result)
            return ResponseEntity.ok(movingPostRequest)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}

data class MovingPostRequest(
    val userID: String,
    val robotLatitude: Double,
    val robotLongitude: Double,
)
