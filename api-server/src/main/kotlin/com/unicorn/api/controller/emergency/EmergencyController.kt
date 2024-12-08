package com.unicorn.api.controller.emergency

import com.unicorn.api.application_service.emergency.SaveEmergencyService
import com.unicorn.api.controller.api_response.ResponseError
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
class EmergencyController(
    private val saveEmergencyService: SaveEmergencyService,
) {
    @PostMapping("/unicorn/emergency")
    fun post(
        @RequestHeader("X-UID") uid: String,
        @RequestBody emergencyPostRequest: EmergencyPostRequest,
    ): ResponseEntity<*> {
        try {
            val result = saveEmergencyService.save(emergencyPostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}

data class EmergencyPostRequest(
    val userID: String,
    val userLatitude: Double,
    val userLongitude: Double,
)
