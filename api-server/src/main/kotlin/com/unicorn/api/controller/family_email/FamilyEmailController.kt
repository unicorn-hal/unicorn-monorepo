package com.unicorn.api.controller.family_email

import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.domain.family_email.FamilyEmailID
import com.unicorn.api.query_service.user.UserQueryService
import com.unicorn.api.query_service.family_email.FamilyEmailQueryService
import com.unicorn.api.application_service.family_email.SaveFamilyEmailService
import com.unicorn.api.application_service.family_email.UpdateFamilyEmailService
import com.unicorn.api.application_service.family_email.DeleteFamilyEmailService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.UUID

@Controller
class FamilyEmailController(
    private val userQueryService: UserQueryService,
    private val familyEmailQueryService: FamilyEmailQueryService,
    private val saveFamilyEmailService: SaveFamilyEmailService,
    private val updateFamilyEmailService: UpdateFamilyEmailService,
    private val deleteFamilyEmailService: DeleteFamilyEmailService
) {
    @GetMapping("/family_emails")
    fun get(@RequestHeader("X-UID") uid: String): ResponseEntity<*> {
        try{
            userQueryService.getOrNullBy(uid)
                ?: return ResponseEntity.status(400).body(ResponseError("User not found"))

            val result = familyEmailQueryService.get(UserID(uid))
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @PostMapping("/family_emails")
    fun post(@RequestHeader("X-UID") uid: String, @RequestBody familyEmailPostRequest: FamilyEmailPostRequest): ResponseEntity<*> {
        try {
            val result = saveFamilyEmailService.save(UserID(uid), familyEmailPostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @PutMapping("/family_emails/{familyEmailID}")
    fun put(@RequestHeader("X-UID") uid: String, @RequestBody familyEmailPutRequest: FamilyEmailPutRequest, @PathVariable familyEmailID: UUID): ResponseEntity<*> {
        try {
            val result = updateFamilyEmailService.update(FamilyEmailID(familyEmailID), UserID(uid), familyEmailPutRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/family_emails/{familyEmailID}")
    fun delete(@RequestHeader("X-UID") uid: String, @PathVariable familyEmailID: String): ResponseEntity<Any> {
        try {
            deleteFamilyEmailService.delete(FamilyEmailID(UUID.fromString(familyEmailID)), UserID(uid))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}       

data class FamilyEmailPostRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val iconImageUrl: String?
)

data class FamilyEmailPutRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val iconImageUrl: String?
)