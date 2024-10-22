package com.unicorn.api.controller.user

import com.unicorn.api.application_service.user.DeleteUserService
import com.unicorn.api.application_service.user.SaveUserService
import com.unicorn.api.application_service.user.UpdateUserService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.query_service.user.UserQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Controller
class UserController(
    private val userQueryService: UserQueryService,
    private val saveUserService: SaveUserService,
    private val updateUserService: UpdateUserService,
    private val deleteUserService: DeleteUserService
) {
    @GetMapping("/users/{userID}")
    fun get(@RequestHeader("X-UID") uid: String, @PathVariable userID: String): ResponseEntity<*> {
        try {
            val result = userQueryService.getOrNullBy(userID)
                ?: return ResponseEntity.status(404).body(ResponseError("Not Found"))

            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @PostMapping("/users")
    fun save(@RequestHeader("X-UID") uid: String, @RequestBody userPostRequest: UserPostRequest): ResponseEntity<*> {
        try {
            val result = saveUserService.save(UID(uid), userPostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @PutMapping("/users/{userID}")
    fun update(
        @RequestHeader("X-UID") uid: String,
        @RequestBody userPutRequest: UserPutRequest,
        @PathVariable userID: String
    ): ResponseEntity<*> {
        try {
            val result = updateUserService.update(UserID(userID), userPutRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/users/{userID}")
    fun delete(@RequestHeader("X-UID") uid: String, @PathVariable userID: String): ResponseEntity<Any> {
        try {
            deleteUserService.delete(UserID(userID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}

data class UserPostRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthDate: LocalDate,
    val gender: String,
    val address: String,
    val postalCode: String,
    val phoneNumber: String,
    val iconImageUrl: String?,
    val bodyHeight: Double,
    val bodyWeight: Double,
    val occupation: String
)

data class UserPutRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthDate: LocalDate,
    val gender: String,
    val address: String,
    val postalCode: String,
    val phoneNumber: String,
    val iconImageUrl: String?,
    val bodyHeight: Double,
    val bodyWeight: Double,
    val occupation: String
)