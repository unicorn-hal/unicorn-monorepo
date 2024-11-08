package com.unicorn.api.controller.primary_doctor

import com.unicorn.api.application_service.primary_doctor.SavePrimaryDoctorService
import com.unicorn.api.application_service.primary_doctor.UpdatePrimaryDoctorService
import com.unicorn.api.controller.api_response.ResponseError
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class PrimaryDoctorController(
    private val savePrimaryDoctorService: SavePrimaryDoctorService,
    private val updatePrimaryDoctorService: UpdatePrimaryDoctorService
) {
    @PostMapping("/primary_doctors")
    fun post(
        @RequestHeader("X-UID") uid: String,
        @RequestBody primaryDoctorPostRequest: PrimaryDoctorRequest
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
        @RequestBody primaryDoctorPutRequest: PrimaryDoctorRequest
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
    val doctorIDs: List<String>
)