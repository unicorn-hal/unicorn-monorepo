package com.unicorn.api.controller.family_email

import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.query_service.user.UserQueryService
import com.unicorn.api.query_service.family_email.FamilyEmailQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
class FamilyEmailController(
    private val userQueryService: UserQueryService,
    private val familyEmailQueryService: FamilyEmailQueryService,
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
}