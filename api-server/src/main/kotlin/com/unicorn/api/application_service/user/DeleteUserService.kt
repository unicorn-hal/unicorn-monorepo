package com.unicorn.api.application_service.user

import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface DeleteUserService {
    fun delete(userID: UserID): Unit
}

@Service
class DeleteUserServiceImpl(
    val userRepository: UserRepository,
) : DeleteUserService {
    override fun delete(userID: UserID) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        userRepository.delete(user)
    }
}
