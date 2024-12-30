package com.unicorn.api.application_service.chat

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.chat.ChatRepository
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface DeleteChatService {
    fun deleteBy(userID: UserID)

    fun deleteByDoctorID(doctorID: DoctorID)
}

@Service
class DeleteChatServiceImpl(
    private val userRepository: UserRepository,
    private val doctorRepository: DoctorRepository,
    private val chatRepository: ChatRepository,
) : DeleteChatService {
    override fun deleteBy(userID: UserID) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        chatRepository.deleteByUser(user)
    }

    override fun deleteByDoctorID(doctorID: DoctorID) {
        val doctor = doctorRepository.getOrNullBy(doctorID)
        requireNotNull(doctor) { "Doctor not found" }

        chatRepository.deleteByDoctor(doctor)
    }
}
