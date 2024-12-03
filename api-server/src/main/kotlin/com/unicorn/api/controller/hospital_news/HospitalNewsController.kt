package com.unicorn.api.controller.hospital_news

import com.unicorn.api.application.hospital.HospitalQueryService
import com.unicorn.api.application_service.hospital_news.DeleteHospitalNewsService
import com.unicorn.api.application_service.hospital_news.SaveHospitalNewsService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.hospital.HospitalID
import com.unicorn.api.domain.hospital_news.HospitalNewsID
import com.unicorn.api.query_service.doctor.DoctorQueryService
import com.unicorn.api.query_service.hospital_news.HospitalNewsQueryService
import com.unicorn.api.query_service.user.UserQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import java.util.*

@Controller
class HospitalNewsController(
    private val deleteHospitalNewsService: DeleteHospitalNewsService,
    private val saveHospitalNewsService: SaveHospitalNewsService,
    private val hospitalNewsQueryService: HospitalNewsQueryService,
    private val doctorQueryService: DoctorQueryService,
    private val userQueryService: UserQueryService,
    private val hospitalQueryService: HospitalQueryService,
) {
    @GetMapping("/hospitals/news")
    fun get(
        @RequestHeader("X-UID") uid: String,
    ): ResponseEntity<*> {
        userQueryService.getOrNullBy(uid)
            ?: return ResponseEntity.status(400).body(ResponseError("Doctor not found"))

        val result = hospitalNewsQueryService.getAll()

        return ResponseEntity.ok(result)
    }

    @GetMapping("/hospitals/{hospitalID}/news")
    fun getBy(
        @RequestHeader("X-UID") uid: String,
        @PathVariable hospitalID: UUID,
    ): ResponseEntity<*> {
        doctorQueryService.getOrNullBy(DoctorID(uid))
            ?: return ResponseEntity.status(400).body(ResponseError("Doctor not found"))

        hospitalQueryService.getBy(hospitalID)
            ?: return ResponseEntity.status(400).body(ResponseError("Hospital not found"))

        val result = hospitalNewsQueryService.getByHospitalID(HospitalID(hospitalID))

        return ResponseEntity.ok(result)
    }

    @PostMapping("/hospitals/{hospitalID}/news")
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
