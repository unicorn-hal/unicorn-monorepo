package com.unicorn.api.domain.chat_support

import com.unicorn.api.domain.doctor.DoctorID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalTime
import java.util.*

class ChatSupportTest {

    @Test
    fun `create chat support`() {
        val doctorID = DoctorID("doctorID")
        val chatSupportStartHour = LocalTime.of(9, 0)
        val chatSupportEndHour = LocalTime.of(17, 0)

        val chatSupport = ChatSupport.create(
            doctorID,
            chatSupportStartHour,
            chatSupportEndHour
        )

        assertEquals(doctorID, chatSupport.doctorID)
        assertEquals(chatSupportStartHour, chatSupport.chatSupportStartHour.value)
        assertEquals(chatSupportEndHour, chatSupport.chatSupportEndHour.value)
    }


    @Test
    fun `should create chat support from store`() {
        val chatSupportID = UUID.randomUUID()
        val doctorID = "doctorID"
        val chatSupportStartHour = LocalTime.of(9, 0)
        val chatSupportEndHour = LocalTime.of(17, 0)

        val chatSupport = ChatSupport.fromStore(
            chatSupportID,
            doctorID,
            chatSupportStartHour,
            chatSupportEndHour
        )

        assertEquals(chatSupportID, chatSupport.chatSupportID.value)
        assertEquals(doctorID, chatSupport.doctorID.value)
        assertEquals(chatSupportStartHour, chatSupport.chatSupportStartHour.value)
        assertEquals(chatSupportEndHour, chatSupport.chatSupportEndHour.value)
    }

    @Test
    fun `should update chat support`() {
        val doctorID = DoctorID("doctorID")
        val chatSupportStartHour = LocalTime.of(9, 0)
        val chatSupportEndHour = LocalTime.of(17, 0)
        val chatSupport = ChatSupport.fromStore(
            UUID.randomUUID(),
            doctorID.value,
            chatSupportStartHour,
            chatSupportEndHour
        )

        val newChatSupportStartHour = LocalTime.of(10, 0)
        val newChatSupportEndHour = LocalTime.of(18, 0)

        val updatedChatSupport = chatSupport.update(
            ChatSupportStartHour(newChatSupportStartHour),
            ChatSupportEndHour(newChatSupportEndHour)
        )

        assertEquals(doctorID, updatedChatSupport.doctorID)
        assertEquals(newChatSupportStartHour, updatedChatSupport.chatSupportStartHour.value)
        assertEquals(newChatSupportEndHour, updatedChatSupport.chatSupportEndHour.value)
    }

    @Test
    fun `should throw exception when creating chat support with start hour after end hour`() {
        val doctorID = DoctorID("doctorID")
        val chatSupportStartHour = LocalTime.of(9, 0)
        val chatSupportEndHour = LocalTime.of(17, 0)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            ChatSupport.create(
                doctorID,
                chatSupportEndHour,
                chatSupportStartHour
            )
        }

        assertEquals("Start hour must be before end hour", exception.message)
    }


    @Test
    fun `should throw exception when updating chat support with start hour after end hour`() {
        val doctorID = DoctorID("doctorID")
        val chatSupportStartHour = LocalTime.of(9, 0)
        val chatSupportEndHour = LocalTime.of(17, 0)

        val chatSupport = ChatSupport.fromStore(
            UUID.randomUUID(),
            doctorID.value,
            chatSupportStartHour,
            chatSupportEndHour
        )

        val newChatSupportStartHour = LocalTime.of(18, 0)
        val newChatSupportEndHour = LocalTime.of(17, 0)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            chatSupport.update(
                ChatSupportStartHour(newChatSupportStartHour),
                ChatSupportEndHour(newChatSupportEndHour)
            )
        }

        assertEquals("Start hour must be before end hour", exception.message)
    }

    @Test
    fun `should throw exception when creating chat support with start hour equal to end hour`() {
        val doctorID = DoctorID("doctorID")
        val chatSupportStartHour = LocalTime.of(9, 0)
        val chatSupportEndHour = LocalTime.of(9, 0)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            ChatSupport.create(
                doctorID,
                chatSupportStartHour,
                chatSupportEndHour
            )
        }

        assertEquals("Start hour must be before end hour", exception.message)
    }

    @Test
    fun `should throw exception when updating chat support with start hour equal to end hour`() {
        val doctorID = DoctorID("doctorID")
        val chatSupportStartHour = LocalTime.of(9, 0)
        val chatSupportEndHour = LocalTime.of(17, 0)
        val chatSupport = ChatSupport.fromStore(
            UUID.randomUUID(),
            doctorID.value,
            chatSupportStartHour,
            chatSupportEndHour
        )
        val newChatSupportStartHour = LocalTime.of(17, 0)
        val newChatSupportEndHour = LocalTime.of(17, 0)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            chatSupport.update(
                ChatSupportStartHour(newChatSupportStartHour),
                ChatSupportEndHour(newChatSupportEndHour)
            )
        }

        assertEquals("Start hour must be before end hour", exception.message)
    }

}