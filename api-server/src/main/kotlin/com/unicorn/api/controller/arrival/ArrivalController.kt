package com.unicorn.api.controller.arrival

import com.unicorn.api.application_service.arrival.NotifyArrivalService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.robot.RobotID
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
class ArrivalController(
    private val notifyArrivalService: NotifyArrivalService,
    private val simpMessagingTemplate: SimpMessagingTemplate,
) {
    @PostMapping("/unicorn/{robotID}/arrival")
    fun post(
        @RequestHeader("X-UID") uid: String,
        @PathVariable robotID: String,
        @RequestBody arrivalPostRequest: ArrivalPostRequest,
    ): ResponseEntity<*> {
        try {
            val result = notifyArrivalService.notify(RobotID(robotID), arrivalPostRequest)
            simpMessagingTemplate.convertAndSend("/topic/unicorn/users/${arrivalPostRequest.userID}", result)
            return ResponseEntity.ok(arrivalPostRequest)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}

data class ArrivalPostRequest(
    val userID: String,
    val robotLatitude: Double,
    val robotLongitude: Double,
)
