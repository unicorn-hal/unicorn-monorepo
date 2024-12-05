package com.unicorn.api.controller.account

import com.unicorn.api.application_service.account.DeleteAccountService
import com.unicorn.api.application_service.account.SaveAccountService
import com.unicorn.api.application_service.account.UpdateAccountService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.account.Account
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.query_service.account.AccountDto
import com.unicorn.api.query_service.account.AccountQueryService
import com.unicorn.api.query_service.doctor.DoctorQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class AccountController(
    private val saveAccountService: SaveAccountService,
    private val accountQueryService: AccountQueryService,
    private val deleteAccountService: DeleteAccountService,
    private val updateAccountService: UpdateAccountService,
    private val doctorQueryService: DoctorQueryService,
) {
    @PostMapping("/accounts")
    fun save(
        @RequestHeader("X-UID") uid: String,
        @RequestBody accountPostRequest: AccountPostRequest,
    ): ResponseEntity<Account> {
        try {
            val result =
                saveAccountService.save(
                    uid = accountPostRequest.uid,
                    role = accountPostRequest.role,
                    fcmTokenId = accountPostRequest.fcmTokenId,
                )
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/accounts")
    fun get(
        @RequestHeader("X-UID") uid: String,
    ): ResponseEntity<AccountDto> {
        try {
            val result =
                accountQueryService.getOrNullBy(uid)
                    ?: return ResponseEntity.notFound().build()

            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/accounts/{accountUid}")
    fun get(
        @RequestHeader("X-UID") uid: String,
        @PathVariable accountUid: String,
    ): ResponseEntity<*> {
        try {
            doctorQueryService.getOrNullBy(DoctorID(uid))
                ?: return ResponseEntity.badRequest().body(ResponseError("account is not doctor"))

            val result =
                accountQueryService.getOrNullBy(accountUid)
                    ?: return ResponseEntity.badRequest().body(ResponseError("account is not found"))

            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError("internal server error"))
        }
    }

    @DeleteMapping("/accounts")
    fun delete(
        @RequestHeader("X-UID") uid: String,
    ): ResponseEntity<Unit> {
        try {
            deleteAccountService.delete(UID(uid))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.notFound().build()
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().build()
        }
    }

    @PutMapping("/accounts")
    fun update(
        @RequestHeader("X-UID") uid: String,
        @RequestBody accountPutRequest: AccountPutRequest,
    ): ResponseEntity<*> {
        try {
            val result = updateAccountService.update(UID(uid), accountPutRequest)

            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(errorType = e.message ?: "invalid request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError(errorType = "internal server error"))
        }
    }
}

data class AccountPostRequest(
    val uid: String,
    val role: String,
    val fcmTokenId: String,
)

data class AccountPutRequest(
    val fcmTokenId: String,
)
