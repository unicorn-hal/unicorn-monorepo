package com.unicorn.api.controller.call

import com.fasterxml.jackson.annotation.JsonFormat
import com.unicorn.api.application_service.call.DeleteCallService
import com.unicorn.api.application_service.call.SaveCallService
import com.unicorn.api.application_service.call.UpdateCallService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.call.CallReservationID
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.query_service.call.CallQueryService
import com.unicorn.api.query_service.doctor.DoctorQueryService
import com.unicorn.api.query_service.user.UserQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Controller
class CallController(
    private val userQueryService: UserQueryService,
    private val doctorQueryService: DoctorQueryService,
    private val callQueryService: CallQueryService,
    private val saveCallService: SaveCallService,
    private val updateCallService: UpdateCallService,
    private val deleteCallService: DeleteCallService,
) {
    @GetMapping("/calls")
    fun get(
        @RequestHeader("X-UID") uid: String,
        @RequestParam doctorID: String,
        @RequestParam userID: String,
    ): ResponseEntity<*> {
        userQueryService.getOrNullBy(userID)
            ?: return ResponseEntity.status(400).body(ResponseError("User not found"))

        doctorQueryService.getOrNullBy(DoctorID(doctorID))
            ?: return ResponseEntity.status(400).body(ResponseError("Doctor not found"))

        val result =
            callQueryService.get(DoctorID(doctorID), UserID(uid))
                ?: return ResponseEntity.status(400).body(ResponseError("No call reservations found"))

        return ResponseEntity.ok(result)
    }

    @GetMapping("/doctors/{doctorID}/calls")
    fun getByDoctorID(
        @RequestHeader("X-UID") uid: String,
        @PathVariable doctorID: String,
    ): ResponseEntity<*> {
        doctorQueryService.getOrNullBy(DoctorID(doctorID))
            ?: return ResponseEntity.status(400).body(ResponseError("Doctor not found"))

        val result =
            callQueryService.getByDoctorID(DoctorID(doctorID))
                ?: return ResponseEntity.status(400).body(ResponseError("No call reservations found"))

        return ResponseEntity.ok(result)
    }

    @PostMapping("/calls")
    fun store(
        @RequestHeader("X-UID") uid: String,
        @RequestBody callPostRequest: CallPostRequest,
    ): ResponseEntity<*> {
        try {
            val result = saveCallService.save(UserID(uid), callPostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @PutMapping("/calls/{callReservationID}")
    fun put(
        @RequestHeader("X-UID") uid: String,
        @RequestBody callPutRequest: CallPostRequest,
        @PathVariable callReservationID: UUID,
    ): ResponseEntity<*> {
        try {
            val result = updateCallService.update(UserID(uid), callPutRequest, callReservationID)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/calls/{callReservationID}")
    fun delete(
        @RequestHeader("X-UID") uid: String,
        @PathVariable callReservationID: UUID,
    ): ResponseEntity<Any> {
        try {
            deleteCallService.delete(CallReservationID(callReservationID), UserID(uid))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}

data class CallPostRequest(
    val doctorID: String,
    val userID: String,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    var callStartTime: OffsetDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    var callEndTime: OffsetDateTime,
) {
    init {
        val jstOffset = ZoneOffset.ofHours(9)
        callStartTime = callStartTime.withOffsetSameInstant(jstOffset)
        callEndTime = callEndTime.withOffsetSameInstant(jstOffset)
    }
}
