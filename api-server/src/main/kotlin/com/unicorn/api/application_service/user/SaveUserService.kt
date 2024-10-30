package com.unicorn.api.application_service.user

import com.unicorn.api.controller.user.UserPostRequest
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.user.User
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.account.AccountRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface SaveUserService {
    fun save(
        uid: UID,
        userPostRequest: UserPostRequest,
    ): User
}

@Service
class SaveUserServiceImpl(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository,
) : SaveUserService {
    override fun save(
        uid: UID,
        userPostRequest: UserPostRequest,
    ): User {
        val account = accountRepository.getOrNullByUid(uid)
        requireNotNull(account) { "Account not found" }
        require(account.isUser()) { "Account is not user" }

        val existingUser = userRepository.getOrNullBy(UserID(account.uid.value))
        require(existingUser == null) { "User already exists" }

        val user =
            User.create(
                userID = uid.value,
                firstName = userPostRequest.firstName,
                lastName = userPostRequest.lastName,
                email = userPostRequest.email,
                birthDate = userPostRequest.birthDate,
                gender = userPostRequest.gender,
                address = userPostRequest.address,
                postalCode = userPostRequest.postalCode,
                phoneNumber = userPostRequest.phoneNumber,
                iconImageUrl = userPostRequest.iconImageUrl,
                bodyHeight = userPostRequest.bodyHeight,
                bodyWeight = userPostRequest.bodyWeight,
                occupation = userPostRequest.occupation,
            )

        userRepository.store(user)

        return user
    }
}
