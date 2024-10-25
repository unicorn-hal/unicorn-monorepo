package com.unicorn.api.domain.family_email

import com.unicorn.api.domain.user.*
import java.util.*

data class FamilyEmail private constructor(
    val familyEmailID: FamilyEmailID,
    val email: Email,
    val firstName: FirstName,
    val lastName: LastName,
    val phoneNumber: PhoneNumber,
    val iconImageUrl: IconImageUrl?
) {
    companion object {
        fun fromStore(
            familyEmailID: UUID,
            email: String,
            firstName: String,
            lastName: String,
            phoneNumber: String,
            iconImageUrl: String?
        ): FamilyEmail {
            return FamilyEmail(
                familyEmailID = FamilyEmailID(familyEmailID),
                email = Email(email),
                firstName = FirstName(firstName),
                lastName = LastName(lastName),
                phoneNumber = PhoneNumber(phoneNumber),
                iconImageUrl = iconImageUrl?.let { IconImageUrl(it) }
            )
        }

        fun create(
            familyEmailID: UUID,
            email: String,
            firstName: String,
            lastName: String,
            phoneNumber: String,
            iconImageUrl: String?
        ): FamilyEmail {
            return FamilyEmail(
                familyEmailID = FamilyEmailID(familyEmailID),
                email = Email(email),
                firstName = FirstName(firstName),
                lastName = LastName(lastName),
                phoneNumber = PhoneNumber(phoneNumber),
                iconImageUrl = iconImageUrl?.let { IconImageUrl(it) }
            )
        }
    }

    fun update(
        email: Email,
        firstName: FirstName,
        lastName: LastName,
        phoneNumber: PhoneNumber,
        iconImageUrl: IconImageUrl?
    ): FamilyEmail {
        return this.copy(
            email = email,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            iconImageUrl = iconImageUrl
        )
    }
}

@JvmInline
value class FamilyEmailID(val value: UUID){
    init{
        require(value != UUID(0L,0L)){"familyEmailID should not be null UUID"}
    }
}

@JvmInline
value class Email(val value: String){
    init{
        require(value.isNotBlank()){"email should not be blank"}
    }
}

@JvmInline
value class FirstName(val value: String){
    init{
        require(value.isNotBlank()){"first name should not be blank"}
    }
}

@JvmInline
value class LastName(val value: String){
    init{
        require(value.isNotBlank()){"last name should not be blank"}
    }
}

@JvmInline
value class PhoneNumber(val value: String){
    init{
        require(value.all{ it.isDigit() }){"phoneNumber should be all digits"}
    }
}

@JvmInline
value class IconImageUrl(val value: String)