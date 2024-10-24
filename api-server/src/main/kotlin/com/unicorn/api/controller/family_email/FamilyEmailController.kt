package com.unicorn.api.controller.family_email

import com.unicorn.api.query_service.family_email.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class FamilyEmailController(
    private val familyEmailQueryService: FamilyEmailQueryService,
) {
    @GetMapping("/family_emails")
    fun getFamilyEmails(@RequestHeader("X-UID") uid: String): ResponseEntity<FamilyEmailResult> {
        try{
            val result = familyEmailQueryService.get(uid)
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.status(500).build()
        }
    }
}