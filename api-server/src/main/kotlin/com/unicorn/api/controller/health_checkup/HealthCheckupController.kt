package com.unicorn.api.controller.health_checkup

import com.fasterxml.jackson.annotation.JsonFormat
import com.unicorn.api.application_service.health_checkup.*
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.health_checkup.HealthCheckupID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.query_service.health_checkup.HealthCheckupQueryService
import com.unicorn.api.query_service.user.UserQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@Controller
class HealthCheckupController(
    private val userQueryService: UserQueryService,
    private val saveHealthCheckupService: SaveHealthCheckupService,
    private val updateHealthCheckupService: UpdateHealthCheckupService,
    private val deleteHealthCheckupService: DeleteHealthCheckupService,
    private val healthCheckupQueryService: HealthCheckupQueryService,
) {
    @GetMapping("/health_checkups")
    fun get(
        @RequestHeader("X-UID") uid: String,
    ): ResponseEntity<*> {
        try {
            userQueryService.getOrNullBy(uid)
                ?: return ResponseEntity.badRequest().body(ResponseError("User not found"))

            val result = healthCheckupQueryService.getBy(UserID(uid))
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

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

    @GetMapping("/users/{userID}/health_checkups/{healthCheckupID}")
    fun get(
        @RequestHeader("X-UID") uid: String,
        @PathVariable healthCheckupID: UUID,
        @PathVariable userID: String,
    ): ResponseEntity<*> {
        try {
            userQueryService.getOrNullBy(userID)
                ?: return ResponseEntity.badRequest().body(ResponseError("User not found"))

            val result =
                healthCheckupQueryService.getOrNullBy(HealthCheckupID(healthCheckupID))
                    ?: return ResponseEntity.status(404).body(ResponseError("Health checkup not found"))
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @PutMapping("users/{userID}/health_checkups/{healthCheckupID}")
    fun put(
        @RequestHeader("X-UID") uid: String,
        @PathVariable healthCheckupID: UUID,
        @PathVariable userID: String,
        @RequestBody healthCheckupPutRequest: HealthCheckupPutRequest,
    ): ResponseEntity<*> {
        try {
            val result = updateHealthCheckupService.update(UserID(userID), HealthCheckupID(healthCheckupID), healthCheckupPutRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/health_checkups/{healthCheckupID}")
    fun delete(
        @RequestHeader("X-UID") uid: String,
        @PathVariable healthCheckupID: UUID,
    ): ResponseEntity<Any> {
        try {
            deleteHealthCheckupService.delete(UserID(uid), HealthCheckupID(healthCheckupID))
            return ResponseEntity.noContent().build()
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
