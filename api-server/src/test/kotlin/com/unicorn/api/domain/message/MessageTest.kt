package com.unicorn.api.domain.message

import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.chat.ChatID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class MessageTest {
    @Test
    fun `should create message`() {
        val chatID = ChatID(UUID.randomUUID())
        val senderID = UID("sender")
        val content = Content("content")

        val message = Message.create(chatID, senderID, content)

        assertEquals(message.chatID, chatID)
        assertEquals(message.senderID, senderID)
        assertEquals(message.content, content)
    }

    @Test
    fun `should create message from store`() {
        val messageID = UUID.randomUUID()
        val chatID = UUID.randomUUID()
        val senderID = "sender"
        val sentAt = LocalDateTime.of(2021, 1, 1, 9, 0, 0)
        val content = "content"

        val message = Message.fromStore(messageID, chatID, senderID, sentAt, content)

        assertEquals(message.messageID.value, messageID)
        assertEquals(message.chatID.value, chatID)
        assertEquals(message.senderID.value, senderID)
        assertEquals(message.sentAt.value, sentAt)
        assertEquals(message.content.value, content)
    }

    @Test
    fun `should not create message with blank content`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Message.create(
                    chatID = ChatID(UUID.randomUUID()),
                    senderID = UID("sender"),
                    content = Content(""),
                )
            }

        assertEquals("Content must not be blank", exception.message)
    }
}
