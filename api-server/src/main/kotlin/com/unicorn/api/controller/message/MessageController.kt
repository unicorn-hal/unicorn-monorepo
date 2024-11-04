package com.unicorn.api.controller.message

import com.unicorn.api.application_service.message.SaveMessageService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.chat.ChatID
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import java.util.UUID

@Controller
class MessageController(
    private val saveMessageService: SaveMessageService,
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
}

data class MessagePostRequest(
    val senderID: String,
    val content: String,
)
