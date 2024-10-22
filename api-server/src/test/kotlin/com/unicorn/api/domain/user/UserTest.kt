package com.unicorn.api.domain.user

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class UserTest {

    @Test
    fun `should create user`() {
        val birthDate = LocalDate.now().minusDays(1)
        val user = User.create(
            userID = "12345",
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            birthDate = birthDate,
            gender = "male",
            address = "123 Main St",
            postalCode = "1234567",
            phoneNumber = "09012345678",
            iconImageUrl = "http://example.com/icon.png",
            bodyHeight = 180.0,
            bodyWeight = 75.0,
            occupation = "Engineer"
        )

        assertEquals("12345", user.userID.value)
        assertEquals("John", user.firstName.value)
        assertEquals("Doe", user.lastName.value)
        assertEquals("john.doe@example.com", user.email.value)
        assertEquals(Gender.male, user.gender)
        assertEquals(birthDate, user.birthDate.value)
        assertEquals("123 Main St", user.address.value)
        assertEquals("1234567", user.postalCode.value)
        assertEquals("09012345678", user.phoneNumber.value)
        assertEquals("http://example.com/icon.png", user.iconImageUrl?.value)
        assertEquals(180.0, user.bodyHeight.value)
        assertEquals(75.0, user.bodyWeight.value)
        assertEquals("Engineer", user.occupation.value)
    }

    @Test
    fun `should not create user with future birth date`() {
        val futureDate = LocalDate.now().plusDays(1)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            User.create(
                userID = "12345",
                firstName = "John",
                lastName = "Doe",
                email = "john.doe@example.com",
                birthDate = futureDate,
                gender = "male",
                address = "123 Main St",
                postalCode = "1234567",
                phoneNumber = "09012345678",
                iconImageUrl = "http://example.com/icon.png",
                bodyHeight = 180.0,
                bodyWeight = 75.0,
                occupation = "Engineer"
            )
        }

        assertEquals("birth date should be in the past", exception.message)
    }

    @Test
    fun `should create user from store`() {
        val birthDate = LocalDate.now().minusDays(1)
        val user = User.fromStore(
            userID = "12345",
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            gender = "male",
            birthDate = birthDate,
            address = "123 Main St",
            postalCode = "1234567",
            phoneNumber = "09012345678",
            iconImageUrl = "http://example.com/icon.png",
            bodyHeight = 180.0,
            bodyWeight = 75.0,
            occupation = "Engineer"
        )

        assertEquals("12345", user.userID.value)
        assertEquals("John", user.firstName.value)
        assertEquals("Doe", user.lastName.value)
        assertEquals("john.doe@example.com", user.email.value)
        assertEquals(Gender.male, user.gender)
        assertEquals(birthDate, user.birthDate.value)
        assertEquals("123 Main St", user.address.value)
        assertEquals("1234567", user.postalCode.value)
        assertEquals("09012345678", user.phoneNumber.value)
        assertEquals("http://example.com/icon.png", user.iconImageUrl?.value)
        assertEquals(180.0, user.bodyHeight.value)
        assertEquals(75.0, user.bodyWeight.value)
        assertEquals("Engineer", user.occupation.value)
    }

    @Test
    fun `should not create user with invalid email`() {
        val birthDate = LocalDate.now()
        val exception = assertThrows(IllegalArgumentException::class.java) {
            User.create(
                userID = "12345",
                firstName = "John",
                lastName = "Doe",
                email = "invalid-email",
                birthDate = birthDate,
                gender = "male",
                address = "123 Main St",
                postalCode = "1234567",
                phoneNumber = "09012345678",
                iconImageUrl = "http://example.com/icon.png",
                bodyHeight = 180.0,
                bodyWeight = 75.0,
                occupation = "Engineer"
            )
        }

        assertEquals("email should be valid", exception.message)
    }

    @Test
    fun `should update user information`() {
        val user = User.create(
            userID = "12345",
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            birthDate = LocalDate.parse("1990-01-01"),
            gender = "male",
            address = "123 Main St",
            postalCode = "1234567",
            phoneNumber = "09012345678",
            iconImageUrl = "http://example.com/icon.png",
            bodyHeight = 180.0,
            bodyWeight = 75.0,
            occupation = "Engineer"
        )

        val updatedUser = user.update(
            firstName = FirstName("Johnny"),
            lastName = LastName("Doe"),
            email = Email("johnny.doe@example.com"),
            birthDate = BirthDate(LocalDate.parse("1990-01-01")),
            address = Address("456 Another St"),
            postalCode = PostalCode("7654321"),
            phoneNumber = PhoneNumber("08087654321"),
            iconImageUrl = IconImageUrl("http://example.com/newicon.png"),
            bodyHeight = BodyHeight(182.0),
            bodyWeight = BodyWeight(78.0),
            occupation = Occupation("Senior Engineer")
        )

        assertEquals("Johnny", updatedUser.firstName.value)
        assertEquals("johnny.doe@example.com", updatedUser.email.value)
        assertEquals("456 Another St", updatedUser.address.value)
        assertEquals("7654321", updatedUser.postalCode.value)
        assertEquals("08087654321", updatedUser.phoneNumber.value)
        assertEquals("http://example.com/newicon.png", updatedUser.iconImageUrl?.value)
        assertEquals(182.0, updatedUser.bodyHeight.value)
        assertEquals(78.0, updatedUser.bodyWeight.value)
        assertEquals("Senior Engineer", updatedUser.occupation.value)
    }
}
