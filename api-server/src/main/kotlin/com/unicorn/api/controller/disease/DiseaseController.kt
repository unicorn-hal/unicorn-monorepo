package com.unicorn.api.controller.disease

import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.query_service.DiseaseQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@Controller
class DiseaseController(
    val diseaseQueryService: DiseaseQueryService,
) {
    @GetMapping("/diseases")
    fun searchDisease(
        @RequestHeader("X-UID") uid: String,
        @RequestParam diseaseName: String,
    ): ResponseEntity<*> {
        try {
            val result = diseaseQueryService.getBy(diseaseName)
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @GetMapping("/diseases/famous")
    fun getFamous(
        @RequestHeader("X-UID") uid: String,
    ): ResponseEntity<*> {
        try {
            val result = diseaseQueryService.getByFamousDisease()
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }
}
