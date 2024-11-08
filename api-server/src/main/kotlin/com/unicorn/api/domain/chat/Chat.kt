package com.unicorn.api.domain.chat

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import java.util.*

data class Chat private constructor(
    val chatID: ChatID,
    val doctorID: DoctorID,
    val userID: UserID,
) {
    companion object {
        fun create(
            doctorID: DoctorID,
            userID: UserID,
        ): Chat {
            return Chat(
                chatID = ChatID(UUID.randomUUID()),
                doctorID = doctorID,
                userID = userID,
            )
        }

        fun fromStore(
            chatID: UUID,
            doctorID: String,
            userID: String,
        ): Chat {
            return Chat(
                chatID = ChatID(chatID),
                doctorID = DoctorID(doctorID),
                userID = UserID(userID),
            )
        }
    }
}

@JvmInline
value class ChatID(val value: UUID)
