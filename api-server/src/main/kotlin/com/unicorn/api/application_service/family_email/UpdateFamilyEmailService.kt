package com.unicorn.api.application_service.family_email

import com.unicorn.api.controller.family_email.FamilyEmailPutRequest
import com.unicorn.api.domain.family_email.*
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.user.UserRepository
import com.unicorn.api.infrastructure.family_email.FamilyEmailRepository
import org.springframework.stereotype.Service

interface UpdateFamilyEmailService{
    fun update(familyEmailID: FamilyEmailID, userID: UserID, familyEmailPutRequest: FamilyEmailPutRequest): FamilyEmail
}

@Service
class UpdateFamilyEmailServiceImpl(
    private val userRepository: UserRepository,
    private val familyEmailRepository: FamilyEmailRepository
) : UpdateFamilyEmailService {

    override fun update(familyEmailID: FamilyEmailID, userID: UserID, familyEmailPutRequest: FamilyEmailPutRequest): FamilyEmail {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val familyEmail = familyEmailRepository.getOrNullBy(familyEmailID)
        requireNotNull(familyEmail) { "Family email not found" }

        val updateFamilyEmail = familyEmail.update(
            email = Email(familyEmailPutRequest.email),
            firstName = FirstName(familyEmailPutRequest.firstName),
            lastName = LastName(familyEmailPutRequest.lastName),
            phoneNumber = PhoneNumber(familyEmailPutRequest.phoneNumber),
            iconImageUrl = familyEmailPutRequest.iconImageUrl?.let { IconImageUrl(it) }
        )
        familyEmailRepository.store(updateFamilyEmail)

        return updateFamilyEmail
    }
}