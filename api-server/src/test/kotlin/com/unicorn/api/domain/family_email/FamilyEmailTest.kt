package com.unicorn.api.domain.family_email

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID

class FamilyEmailTest {
    @Test
    fun `should create family email`() {
        val familyEmail =
            FamilyEmail.create(
                familyEmailID = UUID.randomUUID(),
                userID = "test",
                email = "sample@sample.com",
                firstName = "太郎",
                lastName = "山田",
                iconImageUrl = "http://example.com/icon.png",
            )

        assertEquals(familyEmail.familyEmailID.value, familyEmail.familyEmailID.value)
        assertEquals("test", familyEmail.userID.value)
        assertEquals("sample@sample.com", familyEmail.email.value)
        assertEquals("太郎", familyEmail.firstName.value)
        assertEquals("山田", familyEmail.lastName.value)
        assertEquals("http://example.com/icon.png", familyEmail.iconImageUrl?.value)
    }

    @Test
    fun `should create family email from store`() {
        val familyEmail =
            FamilyEmail.fromStore(
                familyEmailID = UUID.randomUUID(),
                userID = "test",
                email = "sample@sample.com",
                firstName = "太郎",
                lastName = "山田",
                iconImageUrl = "http://example.com/icon.png",
            )

        assertEquals(familyEmail.familyEmailID.value, familyEmail.familyEmailID.value)
        assertEquals("test", familyEmail.userID.value)
        assertEquals("sample@sample.com", familyEmail.email.value)
        assertEquals("太郎", familyEmail.firstName.value)
        assertEquals("山田", familyEmail.lastName.value)
        assertEquals("http://example.com/icon.png", familyEmail.iconImageUrl?.value)
    }

    @Test
    fun `should update family email`() {
        val familyEmail =
            FamilyEmail.create(
                familyEmailID = UUID.randomUUID(),
                userID = "test",
                email = "sample@sample.com",
                firstName = "太郎",
                lastName = "山田",
                iconImageUrl = "http://example.com/icon.png",
            )

        val updatedFamilyEmail =
            familyEmail.update(
                firstName = FirstName("John"),
                lastName = LastName("Doe"),
                email = Email("john.doe@example.com"),
                iconImageUrl = IconImageUrl("http://example.com/newicon.png"),
            )
        assertEquals("John", updatedFamilyEmail.firstName.value)
        assertEquals("Doe", updatedFamilyEmail.lastName.value)
        assertEquals("http://example.com/newicon.png", updatedFamilyEmail.iconImageUrl?.value)
    }

    @Test
    fun `should return an error message when null email`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                FamilyEmail.create(
                    familyEmailID = UUID.randomUUID(),
                    userID = "test",
                    email = "",
                    firstName = "太郎",
                    lastName = "山田",
                    iconImageUrl = "http://example.com/icon.png",
                )
            }
        assertEquals("email should not be blank", exception.message)
    }

    @Test
    fun `should return an error message when null fimilyFirstName`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                FamilyEmail.create(
                    familyEmailID = UUID.randomUUID(),
                    userID = "test",
                    email = "sample@sample.com",
                    firstName = "",
                    lastName = "山田",
                    iconImageUrl = "http://example.com/icon.png",
                )
            }
        assertEquals("first name should not be blank", exception.message)
    }

    @Test
    fun `should return an error message when null fimilyLastName`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                FamilyEmail.create(
                    familyEmailID = UUID.randomUUID(),
                    userID = "test",
                    email = "sample@sample.com",
                    firstName = "太郎",
                    lastName = "",
                    iconImageUrl = "http://example.com/icon.png",
                )
            }
        assertEquals("last name should not be blank", exception.message)
    }
}
