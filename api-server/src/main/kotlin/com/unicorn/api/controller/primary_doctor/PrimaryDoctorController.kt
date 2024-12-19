package com.unicorn.api.controller.primary_doctor

import com.unicorn.api.application_service.primary_doctor.*
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.query_service.doctor.DoctorQueryService
import com.unicorn.api.query_service.primary_doctor.PrimaryDoctorQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
class PrimaryDoctorController(
    private val doctorQueryService: DoctorQueryService,
    private val primaryDoctorQueryService: PrimaryDoctorQueryService,
    private val savePrimaryDoctorService: SavePrimaryDoctorService,
    private val deletePrimaryDoctorService: DeletePrimaryDoctorService,
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
        @RequestBody primaryDoctorRequest: PrimaryDoctorRequest,
    ): ResponseEntity<*> {
        return try {
            val result = savePrimaryDoctorService.save(UserID(uid), primaryDoctorRequest)
            ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/primary_doctors/{doctorID}")
    fun delete(
        @RequestHeader("X-UID") uid: String,
        @PathVariable doctorID: String,
    ): ResponseEntity<Any> {
        try {
            deletePrimaryDoctorService.delete(UserID(uid), DoctorID(doctorID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/users/{userID}/primary_doctors")
    fun deleteBy(
        @RequestHeader("X-UID") uid: String,
        @PathVariable userID: String,
    ): ResponseEntity<Any> {
        try {
            deletePrimaryDoctorService.deleteByUserID(UserID(userID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/doctors/{doctorID}/primary_doctors")
    fun deleteByDoctorID(
        @RequestHeader("X-UID") uid: String,
        @PathVariable doctorID: String,
    ): ResponseEntity<Any> {
        try {
            deletePrimaryDoctorService.deleteByDoctorID(DoctorID(doctorID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }
}

data class PrimaryDoctorRequest(
    val doctorID: String,
)
