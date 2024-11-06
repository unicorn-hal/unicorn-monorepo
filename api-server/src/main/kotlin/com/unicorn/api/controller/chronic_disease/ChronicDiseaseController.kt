package com.unicorn.api.controller.chronic_disease

import com.unicorn.api.application_service.chronic_disease.*
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.chronic_disease.ChronicDiseaseID
import com.unicorn.api.domain.user.UserID
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
class ChronicDiseaseController(
    private val saveChronicDiseaseService: SaveChronicDiseaseService,
    private val deleteChronicDiseaseService: DeleteChronicDiseaseService,
) {
    @PostMapping("/chronic_disease")
    fun post(
        @RequestHeader("X-UID") uid: String,
        @RequestBody chronicDiseasePostRequest: ChronicDiseasePostRequest,
    ): ResponseEntity<*> {
        try {
            val result = saveChronicDiseaseService.save(UserID(uid), chronicDiseasePostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/chronic_disease/{chronicDiseaseID}")
    fun delete(
        @RequestHeader("X-UID") uid: String,
        @PathVariable chronicDiseaseID: UUID,
    ): ResponseEntity<Any> {
        try {
            deleteChronicDiseaseService.delete(UserID(uid), ChronicDiseaseID(chronicDiseaseID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }
}

data class ChronicDiseasePostRequest(
    val diseaseID: Int,
)
