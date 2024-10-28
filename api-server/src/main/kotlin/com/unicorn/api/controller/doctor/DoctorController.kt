package com.unicorn.api.controller.doctor

import com.fasterxml.jackson.annotation.JsonFormat
import com.unicorn.api.application_service.doctor.*
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.department.DepartmentID
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.hospital.HospitalID
import com.unicorn.api.query_service.doctor.DoctorQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalTime
import java.util.*

@Controller
class DoctorController(
    private val saveDoctorService: SaveDoctorService,
    private val updateDoctorService: UpdateDoctorService,
    private val deleteDoctorService: DeleteDoctorService,
    private val doctorQueryService: DoctorQueryService
) {
    @PostMapping("/doctors")
    fun post(
        @RequestHeader("X-UID") uid: String,
        @RequestBody doctorPostRequest: DoctorPostRequest
    ): ResponseEntity<*> {
        try {
            val result = saveDoctorService.save(UID(uid), doctorPostRequest)
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
            val result = updateDoctorService.update(DoctorID(doctorID), doctorPutRequest)
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
            deleteDoctorService.delete(DoctorID(doctorID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @GetMapping("/doctors/{doctorID}")
    fun get(
        @RequestHeader("X-UID") uid: String,
        @PathVariable doctorID: String
    ): ResponseEntity<Any> {
        try {
            val result = doctorQueryService.getOrNullBy(DoctorID(doctorID))
                ?: return ResponseEntity.notFound().build()
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @GetMapping("/hospitals/{hospitalID}/doctors")
    fun getDoctorsByHospital(
        @RequestHeader("X-UID") uid: String,
        @PathVariable hospitalID: UUID
    ): ResponseEntity<Any> {
        try {
            val result = doctorQueryService.getBy(HospitalID(hospitalID))
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @GetMapping("/doctors")
    fun getDoctors(
        @RequestHeader("X-UID") uid: String,
        @RequestParam departmentID: UUID?,
        @RequestParam doctorName: String?,
        @RequestParam hospitalName: String?
    ): ResponseEntity<Any> {
        try {
            val result = doctorQueryService.searchDoctors(departmentID?.let { DepartmentID(it) }, doctorName, hospitalName)
            return ResponseEntity.ok(result)
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