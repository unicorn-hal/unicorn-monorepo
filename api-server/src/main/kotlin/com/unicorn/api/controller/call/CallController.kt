package com.unicorn.api.controller.call

import com.fasterxml.jackson.annotation.JsonFormat
import com.unicorn.api.application_service.call.SaveCallService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.user.UserID
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Controller
class CallController(
    private val saveCallService: SaveCallService,
) {
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
