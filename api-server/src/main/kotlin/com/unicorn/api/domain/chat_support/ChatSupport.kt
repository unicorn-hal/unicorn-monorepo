package com.unicorn.api.domain.chat_support

import com.unicorn.api.domain.doctor.DoctorID
import java.time.LocalTime
import java.util.UUID

data class ChatSupport private constructor(
    val chatSupportID: ChatSupportID,
    val doctorID: DoctorID,
    val chatSupportStartHour: ChatSupportStartHour,
    val chatSupportEndHour: ChatSupportEndHour
) {
    companion object {
        fun fromStore(
            chatSupportID: UUID,
            doctorID: String,
            chatSupportStartHour: LocalTime,
            chatSupportEndHour: LocalTime
        ): ChatSupport {
            return ChatSupport(
                ChatSupportID(chatSupportID),
                DoctorID(doctorID),
                ChatSupportStartHour(chatSupportStartHour),
                ChatSupportEndHour(chatSupportEndHour)
            )
        }

        fun create(
            doctorID: DoctorID,
            chatSupportStartHour: LocalTime,
            chatSupportEndHour: LocalTime
        ): ChatSupport {
            require(chatSupportStartHour.isBefore(chatSupportEndHour)) {
                "Start hour must be before end hour"
            }

            return ChatSupport(
                ChatSupportID(UUID.randomUUID()),
                doctorID,
                ChatSupportStartHour(chatSupportStartHour),
                ChatSupportEndHour(chatSupportEndHour)
            )
        }
    }

    fun update(
        chatSupportStartHour: ChatSupportStartHour,
        chatSupportEndHour: ChatSupportEndHour
    ): ChatSupport {
        require(chatSupportStartHour.value.isBefore(chatSupportEndHour.value)) {
            "Start hour must be before end hour"
        }

        return this.copy(
            chatSupportStartHour = chatSupportStartHour,
            chatSupportEndHour = chatSupportEndHour
        )
    }
}

@JvmInline
value class ChatSupportID(val value: UUID)

@JvmInline
value class ChatSupportStartHour(val value: LocalTime)

@JvmInline
value class ChatSupportEndHour(val value: LocalTime)