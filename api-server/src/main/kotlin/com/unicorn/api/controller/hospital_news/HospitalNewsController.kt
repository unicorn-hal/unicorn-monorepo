package com.unicorn.api.controller.hospital_news

import com.unicorn.api.application_service.hospital_news.DeleteHospitalNewsService
import com.unicorn.api.application_service.hospital_news.SaveHospitalNewsService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.hospital.HospitalID
import com.unicorn.api.domain.hospital_news.HospitalNewsID
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import java.util.*

@Controller
class HospitalNewsController(
    private val deleteHospitalNewsService: DeleteHospitalNewsService,
    private val saveHospitalNewsService: SaveHospitalNewsService,
) {
    @PostMapping("hospitals/{hospitalID}/news")
    fun store(
        @RequestHeader("X-UID") uid: String,
        @PathVariable hospitalID: UUID,
        @RequestBody hospitalNewsRequest: HospitalNewsRequest,
    ): ResponseEntity<*> {
        try {
            val result = saveHospitalNewsService.save(DoctorID(uid), HospitalID(hospitalID), hospitalNewsRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/hospitals/{hospitalID}/news/{newsID}")
    fun delete(
        @RequestHeader("X-UID") uid: String,
        @PathVariable hospitalID: UUID,
        @PathVariable newsID: UUID,
    ): ResponseEntity<Any> {
        try {
            deleteHospitalNewsService.delete(DoctorID(uid), HospitalID(hospitalID), HospitalNewsID(newsID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}

data class HospitalNewsRequest(
    val hospitalID: UUID,
    val title: String,
    val contents: String,
    val noticeImageUrl: String?,
    val relatedUrl: String?,
)
