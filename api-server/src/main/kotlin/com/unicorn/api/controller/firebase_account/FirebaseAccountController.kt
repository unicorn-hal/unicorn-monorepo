package com.unicorn.api.controller.firebase_account

import com.unicorn.api.application_service.firebase_account.DeleteFirebaseAccountService
import com.unicorn.api.application_service.firebase_account.SaveFirebaseAccountService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.robot.RobotID
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@Controller
class FirebaseAccountController(
    val saveFirebaseAccountService: SaveFirebaseAccountService,
    val deleteFirebaseAccountService: DeleteFirebaseAccountService,
) {
    @PostMapping("/firebase/accounts")
    fun save(
        @RequestHeader("X-UID") uid: String,
        @RequestBody firebaseAccountPostRequest: FirebaseAccountPostRequest,
    ): ResponseEntity<*> {
        try {
            val result = saveFirebaseAccountService.save(DoctorID(uid), firebaseAccountPostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "badRequest"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError(e.message ?: "serverError"))
        }
    }

    @DeleteMapping("/firebase/accounts/{robotID}")
    fun delete(
        @RequestHeader("X-UID") uid: String,
        @PathVariable robotID: String,
    ): ResponseEntity<Any> {
        try {
            deleteFirebaseAccountService.delete(DoctorID(uid), RobotID(robotID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "badRequest"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body(ResponseError(e.message ?: "serverError"))
        }
    }
}

data class FirebaseAccountPostRequest(
    val email: String,
    val password: String,
)

data class FirebaseAccountDto(
    val email: String,
    val password: String,
    val uid: String,
)
