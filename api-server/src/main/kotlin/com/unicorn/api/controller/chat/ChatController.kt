package com.unicorn.api.controller.chat

import com.unicorn.api.application_service.chat.DeleteChatService
import com.unicorn.api.application_service.chat.SaveChatService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.query_service.chat.ChatQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@Controller
class ChatController(
    private val chatQueryService: ChatQueryService,
    private val saveChatService: SaveChatService,
    private val deleteChatService: DeleteChatService,
) {
    @PostMapping("/chats")
    fun postChat(
        @RequestHeader("X-UID") uid: String,
        @RequestBody chatPostRequest: ChatPostRequest,
    ): ResponseEntity<*> {
        try {
            val result = saveChatService.save(chatPostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @GetMapping("/chats")
    fun getChat(
        @RequestHeader("X-UID") uid: String,
    ): ResponseEntity<*> {
        try {
            val result = chatQueryService.getBy(UID(uid))
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/users/{userID}/chats")
    fun deleteByUserID(
        @RequestHeader("X-UID") uid: String,
        @PathVariable userID: String,
    ): ResponseEntity<Any> {
        try {
            deleteChatService.deleteBy(UserID(userID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/doctors/{doctorID}/chats")
    fun deleteByDoctorID(
        @RequestHeader("X-UID") uid: String,
        @PathVariable doctorID: String,
    ): ResponseEntity<Any> {
        try {
            deleteChatService.deleteByDoctorID(DoctorID(doctorID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}

data class ChatPostRequest(
    val doctorID: String,
    val userID: String,
)
