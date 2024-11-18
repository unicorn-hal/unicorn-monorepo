package com.unicorn.api.controller.notification

import com.unicorn.api.application_service.notification.*
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.user.UserID
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class NotificationController(
    private val saveNotificationService: SaveNotificationService,
    private val updateNotificationService: UpdateNotificationService,
) {
    @PostMapping("/users/{userID}/notification")
    fun post(
        @RequestHeader("X-UID") uid: String,
        @PathVariable userID: String,
        @RequestBody notificationPostRequest: NotificationPostRequest,
    ): ResponseEntity<*> {
        try {
            val result = saveNotificationService.save(UserID(userID), notificationPostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @PutMapping("/users/{userID}/notification")
    fun put(
        @RequestHeader("X-UID") uid: String,
        @PathVariable userID: String,
        @RequestBody notificationPutRequest: NotificationPutRequest,
    ): ResponseEntity<*> {
        try {
            val result = updateNotificationService.update(UserID(userID), notificationPutRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}

data class NotificationPostRequest(
    val isMedicineReminder: Boolean,
    val isRegularHealthCheckup: Boolean,
    val isHospitalNews: Boolean,
)

data class NotificationPutRequest(
    val isMedicineReminder: Boolean,
    val isRegularHealthCheckup: Boolean,
    val isHospitalNews: Boolean,
)
