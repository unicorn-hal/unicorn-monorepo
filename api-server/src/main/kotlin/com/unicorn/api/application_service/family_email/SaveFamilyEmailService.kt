package com.unicorn.api.application_service.family_email

import com.unicorn.api.controller.family_email.FamilyEmailPostRequest
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.domain.family_email.FamilyEmail
import com.unicorn.api.infrastructure.user.UserRepository
import com.unicorn.api.infrastructure.family_email.FamilyEmailRepository
import java.util.*
import org.springframework.stereotype.Service

interface SaveFamilyEmailService {
    fun save(userID: UserID, familyEmailPostRequest: FamilyEmailPostRequest): FamilyEmail
}

@Service
class SaveFamilyEmailServiceImpl(
    private val userRepository: UserRepository,
    private val familyEmailRepository: FamilyEmailRepository
) : SaveFamilyEmailService {

    override fun save(userID: UserID, familyEmailPostRequest: FamilyEmailPostRequest): FamilyEmail {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val familyEmail = FamilyEmail.create(
            userID = userID.value,
            email = familyEmailPostRequest.email,
            firstName = familyEmailPostRequest.firstName,
            lastName = familyEmailPostRequest.lastName,
            phoneNumber = familyEmailPostRequest.phoneNumber,
            iconImageUrl = familyEmailPostRequest.iconImageUrl
        )

        familyEmailRepository.store(familyEmail)

        return familyEmail
    }
}