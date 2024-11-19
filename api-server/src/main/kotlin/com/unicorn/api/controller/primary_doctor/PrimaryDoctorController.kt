package com.unicorn.api.controller.primary_doctor

import com.unicorn.api.application_service.primary_doctor.SavePrimaryDoctorService
import com.unicorn.api.application_service.primary_doctor.UpdatePrimaryDoctorService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.query_service.doctor.DoctorQueryService
import com.unicorn.api.query_service.primary_doctor.PrimaryDoctorQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class PrimaryDoctorController(
    private val doctorQueryService: DoctorQueryService,
    private val primaryDoctorQueryService: PrimaryDoctorQueryService,
    private val savePrimaryDoctorService: SavePrimaryDoctorService,
    private val updatePrimaryDoctorService: UpdatePrimaryDoctorService,
) {
    @GetMapping("/primary_doctors")
    fun get(
        @RequestHeader("X-UID") uid: String,
    ): ResponseEntity<*> {
        return try {
            val result = primaryDoctorQueryService.getBy(uid)
            ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError("Primary doctor not found for the specified UID"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @GetMapping("/primary_doctors/{doctorID}/users")
    fun getUsers(
        @RequestHeader("X-UID") uid: String,
        @PathVariable doctorID: String,
    ): ResponseEntity<*> {
        return try {
            val doctor = doctorQueryService.getOrNullBy(DoctorID(doctorID))
            requireNotNull(doctor) { "Doctor with ID $doctorID not found." }

            val result = primaryDoctorQueryService.getUsersBy(DoctorID(doctorID))
            ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @PostMapping("/primary_doctors")
    fun post(
        @RequestHeader("X-UID") uid: String,
        @RequestBody primaryDoctorPostRequest: PrimaryDoctorRequest,
    ): ResponseEntity<*> {
        try {
            val result = savePrimaryDoctorService.save(uid, primaryDoctorPostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @PutMapping("/primary_doctors")
    fun put(
        @RequestHeader("X-UID") uid: String,
        @RequestBody primaryDoctorPutRequest: PrimaryDoctorRequest,
    ): ResponseEntity<*> {
        try {
            val result = updatePrimaryDoctorService.update(uid, primaryDoctorPutRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }
}

data class PrimaryDoctorRequest(
    val doctorIDs: List<String>,
)
