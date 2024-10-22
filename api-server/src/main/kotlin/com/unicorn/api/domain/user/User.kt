package com.unicorn.api.domain.user

import java.time.LocalDate
import java.util.Date

data class User private constructor(
    val userID: UserID,
    val firstName: FirstName,
    val lastName: LastName,
    val email: Email,
    val gender: Gender,
    val birthDate: BirthDate,
    val address: Address,
    val postalCode: PostalCode,
    val phoneNumber: PhoneNumber,
    val iconImageUrl: IconImageUrl?,
    val bodyHeight: BodyHeight,
    val bodyWeight: BodyWeight,
    val occupation: Occupation
) {
    companion object {
        fun fromStore(
            userID: String,
            firstName: String,
            lastName: String,
            email: String,
            gender: String,
            birthDate: LocalDate,
            address: String,
            postalCode: String,
            phoneNumber: String,
            iconImageUrl: String?,
            bodyHeight: Double,
            bodyWeight: Double,
            occupation: String
        ): User {
            return User(
                userID = UserID(userID),
                firstName = FirstName(firstName),
                lastName = LastName(lastName),
                email = Email(email),
                gender = Gender.valueOf(gender),
                birthDate = BirthDate(birthDate),
                address = Address(address),
                postalCode = PostalCode(postalCode),
                phoneNumber = PhoneNumber(phoneNumber),
                iconImageUrl = iconImageUrl?.let { IconImageUrl(it) },
                bodyHeight = BodyHeight(bodyHeight),
                bodyWeight = BodyWeight(bodyWeight),
                occupation = Occupation(occupation)
            )
        }

        fun create(
            userID: String,
            firstName: String,
            lastName: String,
            email: String,
            birthDate: LocalDate,
            gender: String,
            address: String,
            postalCode: String,
            phoneNumber: String,
            iconImageUrl: String?,
            bodyHeight: Double,
            bodyWeight: Double,
            occupation: String
        ): User {
            return User(
                userID = UserID(userID),
                firstName = FirstName(firstName),
                lastName = LastName(lastName),
                email = Email(email),
                birthDate = BirthDate(birthDate),
                gender = Gender.valueOf(gender),
                address = Address(address),
                postalCode = PostalCode(postalCode),
                phoneNumber = PhoneNumber(phoneNumber),
                iconImageUrl = iconImageUrl?.let { IconImageUrl(it) },
                bodyHeight = BodyHeight(bodyHeight),
                bodyWeight = BodyWeight(bodyWeight),
                occupation = Occupation(occupation)
            )
        }
    }

    fun update(
        firstName: FirstName,
        lastName: LastName,
        email: Email,
        birthDate: BirthDate,
        address: Address,
        postalCode: PostalCode,
        phoneNumber: PhoneNumber,
        iconImageUrl: IconImageUrl?,
        bodyHeight: BodyHeight,
        bodyWeight: BodyWeight,
        occupation: Occupation
    ): User {
        return this.copy(
            firstName = firstName,
            lastName = lastName,
            email = email,
            birthDate = birthDate,
            address = address,
            postalCode = postalCode,
            phoneNumber = phoneNumber,
            iconImageUrl = iconImageUrl,
            bodyHeight = bodyHeight,
            bodyWeight = bodyWeight,
            occupation = occupation
        )
    }
}

@JvmInline
value class UserID(val value: String) {
    init {
        require(value.isNotBlank()) { "userID should not be blank" }
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
value class Email(val value: String) {
    init {
        require(value.contains("@")) { "email should be valid" }
    }
}

@JvmInline
value class BirthDate(val value: LocalDate) {
    init {
        require(value.isBefore(LocalDate.now())) { "birth date should be in the past" }
    }
}

@JvmInline
value class Address(val value: String)

@JvmInline
value class PostalCode(val value: String) {
    init {
        require(value.length == 7) { "postal code should be 7 characters" }
        require(value.all { it.isDigit() }) { "postal code should be all digits" }
    }
}

@JvmInline
value class PhoneNumber(val value: String) {
    init {
        require(value.all { it.isDigit() }) { "phone number should be all digits" }
    }
}

@JvmInline
value class IconImageUrl(val value: String)

@JvmInline
value class BodyHeight(val value: Double) {
    init {
        require(value > 0) { "height should be greater than 0" }
    }
}

@JvmInline
value class BodyWeight(val value: Double) {
    init {
        require(value > 0) { "weight should be greater than 0" }
    }
}

@JvmInline
value class Occupation(val value: String)

enum class Gender {
    male, female, other
}