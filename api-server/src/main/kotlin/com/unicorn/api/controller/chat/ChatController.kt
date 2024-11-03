package com.unicorn.api.controller.chat

import com.unicorn.api.application_service.chat.SaveChatService
import com.unicorn.api.controller.api_response.ResponseError
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@Controller
class ChatController(
    private val saveChatService: SaveChatService,
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
}

data class ChatPostRequest(
    val doctorID: String,
    val userID: String,
)
