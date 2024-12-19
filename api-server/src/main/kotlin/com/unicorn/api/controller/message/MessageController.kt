package com.unicorn.api.controller.message

import com.unicorn.api.application_service.message.DeleteMessageService
import com.unicorn.api.application_service.message.SaveMessageService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.chat.ChatID
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.message.MessageID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.query_service.message.MessageQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import java.util.UUID

@Controller
class MessageController(
    private val saveMessageService: SaveMessageService,
    private val deleteMessageService: DeleteMessageService,
    private val messageQueryService: MessageQueryService,
) {
    @PostMapping("/chats/{chatID}/messages")
    fun postMessage(
        @RequestHeader("X-UID") uid: String,
        @PathVariable chatID: UUID,
        @RequestBody messagePostRequest: MessagePostRequest,
    ): ResponseEntity<*> {
        try {
            val result = saveMessageService.save(ChatID(chatID), messagePostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/chats/{chatID}/messages/{messageID}")
    fun deleteMessage(
        @RequestHeader("X-UID") uid: String,
        @PathVariable chatID: UUID,
        @PathVariable messageID: UUID,
    ): ResponseEntity<Any> {
        try {
            deleteMessageService.delete(UID(uid), ChatID(chatID), MessageID(messageID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/users/{userID}/messages")
    fun deleteMessageByUserID(
        @RequestHeader("X-UID") uid: String,
        @PathVariable userID: String,
    ): ResponseEntity<Any> {
        try {
            deleteMessageService.deleteBy(UserID(userID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/doctors/{doctorID}/messages")
    fun deleteMessageByDoctorID(
        @RequestHeader("X-UID") uid: String,
        @PathVariable doctorID: String,
    ): ResponseEntity<Any> {
        try {
            deleteMessageService.deleteByDoctorID(DoctorID(doctorID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @GetMapping("/chats/{chatID}/messages")
    fun getMessages(
        @RequestHeader("X-UID") uid: String,
        @PathVariable chatID: UUID,
    ): ResponseEntity<*> {
        try {
            val result = messageQueryService.getBy(ChatID(chatID))
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}

data class MessagePostRequest(
    val senderID: String,
    val content: String,
)
