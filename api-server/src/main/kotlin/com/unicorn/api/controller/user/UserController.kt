package com.unicorn.api.controller.user

import com.unicorn.api.application_service.user.SaveUserService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.account.UID
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import java.time.LocalDate

@Controller
class UserController(
    private val saveUserService: SaveUserService
) {
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