package com.unicorn.api.domain.chat

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class ChatTest {
    @Test
    fun `should create chat`() {
        val doctorID = DoctorID("doctor")
        val userID = UserID("user")

        val chat = Chat.create(doctorID, userID)

        assertEquals(chat.doctorID, doctorID)
        assertEquals(chat.userID, userID)
    }

    @Test
    fun `should create chat from store`() {
        val chatID = UUID.randomUUID()
        val doctorID = "doctor"
        val userID = "user"

        val chat = Chat.fromStore(chatID, doctorID, userID)

        assertEquals(chat.chatID.value, chatID)
        assertEquals(chat.doctorID.value, doctorID)
        assertEquals(chat.userID.value, userID)
    }
}
