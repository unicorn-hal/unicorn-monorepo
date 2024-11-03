package com.unicorn.api.application_service.chat

import com.unicorn.api.controller.chat.ChatPostRequest
import com.unicorn.api.domain.chat.Chat
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.chat.ChatRepository
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface SaveChatService {
    fun save(chatPostRequest: ChatPostRequest): ChatPostRequest
}

@Service
class SaveChatServiceImpl(
    private val doctorRepository: DoctorRepository,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
) : SaveChatService {
    override fun save(chatPostRequest: ChatPostRequest): ChatPostRequest {
        val doctorID = DoctorID(chatPostRequest.doctorID)
        val doctor = doctorRepository.getOrNullBy(doctorID)
        requireNotNull(doctor) { "Doctor not found" }

        val userID = UserID(chatPostRequest.userID)
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val chat = Chat.create(doctor.doctorID, user.userID)

        chatRepository.store(chat)

        return chatPostRequest
    }
}
