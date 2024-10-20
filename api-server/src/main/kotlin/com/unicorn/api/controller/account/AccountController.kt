package com.unicorn.api.controller.account

import com.unicorn.api.application_service.account.SaveAccountService
import com.unicorn.api.domain.account.Account
import com.unicorn.api.query_service.account.AccountDto
import com.unicorn.api.query_service.account.AccountQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@Controller
class AccountController(
    private val saveAccountService: SaveAccountService,
    private val accountQueryService: AccountQueryService,
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

    @GetMapping("/accounts")
    fun get(@RequestHeader("X-UID") uid: String): ResponseEntity<AccountDto> {
        try {
            val result = accountQueryService.getOrNullBy(uid)
                ?: return ResponseEntity.notFound().build()

            return ResponseEntity.ok(result)
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