package com.unicorn.api.application_service.user

import com.unicorn.api.controller.user.UserPutRequest
import com.unicorn.api.domain.user.*
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface UpdateUserService {
    fun update(userID: UserID, userPutRequest: UserPutRequest): User
}

@Service
class UpdateUserServiceImpl(
    private val userRepository: UserRepository
) : UpdateUserService {
    override fun update(userID: UserID, userPutRequest: UserPutRequest): User {
        val user = userRepository.getOrNullBy(userID)

        requireNotNull(user) {
            "User not found"
        }

        val updateUser = user.update(
            firstName = FirstName(userPutRequest.firstName),
            lastName = LastName(userPutRequest.lastName),
            email = Email(userPutRequest.email),
            birthDate = BirthDate(userPutRequest.birthDate),
            gender = Gender.valueOf(userPutRequest.gender),
            address = Address(userPutRequest.address),
            postalCode = PostalCode(userPutRequest.postalCode),
            phoneNumber = PhoneNumber(userPutRequest.phoneNumber),
            iconImageUrl = userPutRequest.iconImageUrl?.let { IconImageUrl(it) },
            bodyHeight = BodyHeight(userPutRequest.bodyHeight),
            bodyWeight = BodyWeight(userPutRequest.bodyWeight),
            occupation = Occupation(userPutRequest.occupation)
        )

        userRepository.store(updateUser)

        return updateUser
    }
}