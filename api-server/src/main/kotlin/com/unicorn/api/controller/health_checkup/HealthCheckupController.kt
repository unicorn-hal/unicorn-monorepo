package com.unicorn.api.controller.health_checkup

import com.fasterxml.jackson.annotation.JsonFormat
import com.unicorn.api.application_service.health_checkup.*
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.health_checkup.HealthCheckupID
import com.unicorn.api.domain.user.UserID
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@Controller
class HealthCheckupController(
    private val saveHealthCheckupService: SaveHealthCheckupService,
    private val updateHealthCheckupService: UpdateHealthCheckupService,
) {
    @PostMapping("/health_checkups")
    fun post(
        @RequestHeader("X-UID") uid: String,
        @RequestBody healthCheckupPostRequest: HealthCheckupPostRequest,
    ): ResponseEntity<*> {
        try {
            val result = saveHealthCheckupService.save(UserID(uid), healthCheckupPostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @PutMapping("/health_checkups/{healthCheckupID}")
    fun put(
        @RequestHeader("X-UID") uid: String,
        @PathVariable healthCheckupID: UUID,
        @RequestBody healthCheckupPutRequest: HealthCheckupPutRequest,
    ): ResponseEntity<*> {
        try {
            val result = updateHealthCheckupService.update(UserID(uid), HealthCheckupID(healthCheckupID), healthCheckupPutRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }
}

data class HealthCheckupPostRequest(
    val bodyTemperature: Double,
    val bloodPressure: String,
    val medicalRecord: String,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val date: LocalDate,
)

data class HealthCheckupPutRequest(
    val bodyTemperature: Double,
    val bloodPressure: String,
    val medicalRecord: String,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val date: LocalDate,
)
