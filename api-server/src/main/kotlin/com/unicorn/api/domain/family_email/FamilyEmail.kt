package com.unicorn.api.domain.family_email

import com.unicorn.api.domain.user.UserID
import java.util.*

data class FamilyEmail private constructor(
    val familyEmailID: FamilyEmailID,
    val userID: UserID,
    val email: Email,
    val firstName: FirstName,
    val lastName: LastName,
    val iconImageUrl: IconImageUrl?,
) {
    companion object {
        fun fromStore(
            familyEmailID: UUID,
            userID: String,
            email: String,
            firstName: String,
            lastName: String,
            iconImageUrl: String?,
        ): FamilyEmail {
            return FamilyEmail(
                familyEmailID = FamilyEmailID(familyEmailID),
                userID = UserID(userID),
                email = Email(email),
                firstName = FirstName(firstName),
                lastName = LastName(lastName),
                iconImageUrl = iconImageUrl?.let { IconImageUrl(it) },
            )
        }

        fun create(
            userID: String,
            email: String,
            firstName: String,
            lastName: String,
            iconImageUrl: String?,
        ): FamilyEmail {
            return FamilyEmail(
                familyEmailID = FamilyEmailID(UUID.randomUUID()),
                userID = UserID(userID),
                email = Email(email),
                firstName = FirstName(firstName),
                lastName = LastName(lastName),
                iconImageUrl = iconImageUrl?.let { IconImageUrl(it) },
            )
        }
    }

    fun update(
        email: Email,
        firstName: FirstName,
        lastName: LastName,
        iconImageUrl: IconImageUrl?,
    ): FamilyEmail {
        return this.copy(
            email = email,
            firstName = firstName,
            lastName = lastName,
            iconImageUrl = iconImageUrl,
        )
    }
}

@JvmInline
value class FamilyEmailID(val value: UUID) {
    init {
        require(value != UUID(0L, 0L)) { "familyEmailID should not be null UUID" }
    }
}

@JvmInline
value class Email(val value: String) {
    init {
        require(value.isNotBlank()) { "email should not be blank" }
    }
}

@JvmInline
value class FirstName(val value: String) {
    init {
        require(value.isNotBlank()) { "first name should not be blank" }
    }
}

@JvmInline
value class LastName(val value: String) {
    init {
        require(value.isNotBlank()) { "last name should not be blank" }
    }
}

@JvmInline
value class IconImageUrl(val value: String)
