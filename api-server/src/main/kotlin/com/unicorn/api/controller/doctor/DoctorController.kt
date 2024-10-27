package com.unicorn.api.controller.doctor

import com.fasterxml.jackson.annotation.JsonFormat
import com.unicorn.api.application_service.doctor.DoctorDeleteService
import com.unicorn.api.application_service.doctor.DoctorSaveService
import com.unicorn.api.application_service.doctor.DoctorUpdateService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.doctor.DoctorID
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalTime
import java.util.*

@Controller
class DoctorController(
    private val doctorSaveService: DoctorSaveService,
    private val doctorUpdateService: DoctorUpdateService,
    private val doctorDeleteService: DoctorDeleteService
) {
    @PostMapping("/doctors")
    fun post(
        @RequestHeader("X-UID") uid: String,
        @RequestBody doctorPostRequest: DoctorPostRequest
    ): ResponseEntity<*> {
        try {
            val result = doctorSaveService.save(UID(uid), doctorPostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @PutMapping("/doctors/{doctorID}")
    fun put(
        @RequestHeader("X-UID") uid: String,
        @PathVariable doctorID: String,
        @RequestBody doctorPutRequest: DoctorPutRequest
    ): ResponseEntity<*> {
        try {
            val result = doctorUpdateService.update(DoctorID(doctorID), doctorPutRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/doctors/{doctorID}")
    fun delete(
        @RequestHeader("X-UID") uid: String,
        @PathVariable doctorID: String
    ): ResponseEntity<Any> {
        try {
            doctorDeleteService.delete(DoctorID(doctorID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }
}

data class DoctorPostRequest(
    val hospitalID: UUID,
    val email: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val doctorIconUrl: String?,
    val departments: List<UUID>,
    @JsonFormat(pattern = "HH:mm")
    val chatSupportStartHour: LocalTime,
    @JsonFormat(pattern = "HH:mm")
    val chatSupportEndHour: LocalTime,
    @JsonFormat(pattern = "HH:mm")
    val callSupportStartHour: LocalTime,
    @JsonFormat(pattern = "HH:mm")
    val callSupportEndHour: LocalTime,
)

data class DoctorPutRequest(
    val hospitalID: UUID,
    val email: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val doctorIconUrl: String?,
    val departments: List<UUID>,
    @JsonFormat(pattern = "HH:mm")
    val chatSupportStartHour: LocalTime,
    @JsonFormat(pattern = "HH:mm")
    val chatSupportEndHour: LocalTime,
    @JsonFormat(pattern = "HH:mm")
    val callSupportStartHour: LocalTime,
    @JsonFormat(pattern = "HH:mm")
    val callSupportEndHour: LocalTime,
)