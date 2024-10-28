package com.unicorn.api.controller.doctor

import com.fasterxml.jackson.annotation.JsonFormat
import com.unicorn.api.application_service.doctor.DoctorSaveService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.account.UID
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import java.time.LocalTime
import java.util.*

@Controller
class DoctorController(
    private val doctorSaveService: DoctorSaveService
) {
    @PostMapping("/doctors")
    fun post(@RequestHeader("X-UID") uid: String, @RequestBody doctorPostRequest: DoctorPostRequest): ResponseEntity<*> {
        try {
            val result = doctorSaveService.save(UID(uid), doctorPostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError( "Internal Server Error"))
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