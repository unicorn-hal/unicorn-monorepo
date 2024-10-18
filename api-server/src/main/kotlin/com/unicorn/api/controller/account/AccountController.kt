package com.unicorn.api.controller.account

import com.unicorn.api.application_service.account.SaveAccountService
import com.unicorn.api.domain.account.Account
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@Controller
class AccountController(
    private val saveAccountService: SaveAccountService,
) {
    @PostMapping("/accounts")
    fun save(@RequestHeader("X-UID") uid: String, @RequestBody accountPostRequest: AccountPostRequest): ResponseEntity<Account> {
        try {
            val result = saveAccountService.save(
                uid = accountPostRequest.uid,
                role = accountPostRequest.role,
                fcmTokenId = accountPostRequest.fcmTokenId
            )
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().build()
        }
    }
}

data class AccountPostRequest(
    val uid: String,
    val role: String,
    val fcmTokenId: String
)