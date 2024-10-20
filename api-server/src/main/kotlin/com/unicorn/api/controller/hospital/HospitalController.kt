package com.unicorn.api.controller.hospital

import com.unicorn.api.application.hospital.HospitalQueryService
import com.unicorn.api.application.hospital.HospitalDto
import com.unicorn.api.application.hospital.HospitalResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID

@RestController
class HospitalController(
    private val hospitalQueryService: HospitalQueryService
) {
    @GetMapping("/hospitals")
    fun getHospitals(@RequestHeader("X-UID") uid: String): ResponseEntity<HospitalResult> {
        return try {
            val result = hospitalQueryService.getHospitals()
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            ResponseEntity.status(500).build()
        }
    }

    @GetMapping("/hospitals/{hospitalID}")
    fun getHospitalById(@RequestHeader("X-UID") uid: String, @PathVariable hospitalID: UUID): ResponseEntity<HospitalDto> {
        return try {
            val hospital = hospitalQueryService.getBy(hospitalID)
            if (hospital != null) {
                ResponseEntity.ok(hospital)
            } else {
                ResponseEntity.status(404).build()
            }
        }catch (e: Exception) {
            ResponseEntity.status(500).build()
        }
    }
}
