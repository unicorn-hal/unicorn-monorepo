package com.unicorn.api.controller.chronic_disease

import com.unicorn.api.application_service.chronic_disease.*
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.chronic_disease.ChronicDiseaseID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.query_service.chronic_disease.ChronicDiseaseQueryService
import com.unicorn.api.query_service.user.UserQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
class ChronicDiseaseController(
    private val chronicDiseaseQueryService: ChronicDiseaseQueryService,
    private val userQueryService: UserQueryService,
    private val saveChronicDiseaseService: SaveChronicDiseaseService,
    private val deleteChronicDiseaseService: DeleteChronicDiseaseService,
) {
    @GetMapping("/chronic_diseases")
    fun get(
        @RequestHeader("X-UID") uid: String,
    ): ResponseEntity<*> {
        try {
            userQueryService.getOrNullBy(uid)
                ?: return ResponseEntity.status(400).body(ResponseError("User not found"))
            val result = chronicDiseaseQueryService.get(UserID(uid))
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("Internal Server Error"))
        }
    }

    @PostMapping("/chronic_diseases")
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

    @DeleteMapping("/chronic_diseases/{chronicDiseaseID}")
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

    @DeleteMapping("/users/{userID}/chronic_diseases")
    fun deleteByUserID(
        @RequestHeader("X-UID") uid: String,
        @PathVariable userID: String,
    ): ResponseEntity<Any> {
        try {
            deleteChronicDiseaseService.deleteBy(UserID(userID))
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
