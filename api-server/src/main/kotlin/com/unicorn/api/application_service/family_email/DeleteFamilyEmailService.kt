package com.unicorn.api.application_service.family_email

import com.unicorn.api.domain.family_email.FamilyEmailID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.family_email.FamilyEmailRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface DeleteFamilyEmailService {
    fun delete(
        familyEmailID: FamilyEmailID,
        userID: UserID,
    ): Unit
}

@Service
class DeleteFamilyEmailServiceImpl(
    private val familyEmailRepository: FamilyEmailRepository,
    private val userRepository: UserRepository,
) : DeleteFamilyEmailService {
    override fun delete(
        familyEmailID: FamilyEmailID,
        userID: UserID,
    ) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val familyEmail = familyEmailRepository.getOrNullBy(familyEmailID)
        requireNotNull(familyEmail) { "Family email not found" }

        familyEmailRepository.delete(familyEmail)
    }
}
