package com.unicorn.api.domain.family_email

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID

class FamilyEmailTest {

    @Test
    fun `should create family email`() {
        val familyEmail = FamilyEmail.create(
            familyEmailID = UUID.randomUUID(),
            email = "sample@sample.com",
            familyFirstName = "太郎",
            familyLastName = "山田",
            phoneNumber = "09012345678",
            iconImageUrl = "http://example.com/icon.png"
        )
        assertEquals(familyEmail.familyEmailID.value, familyEmail.familyEmailID.value)
        assertEquals("sample@sample.com", familyEmail.email.value)
        assertEquals("太郎", familyEmail.familyFirstName.value)
        assertEquals("山田", familyEmail.familyLastName.value)
        assertEquals("09012345678", familyEmail.phoneNumber.value)
        assertEquals("http://example.com/icon.png", familyEmail.iconImageUrl?.value)
    }

    @Test
    fun `should create family email from store`() {
        val familyEmail = FamilyEmail.fromStore(
            familyEmailID = UUID.randomUUID(),
            email = "sample@sample.com",
            familyFirstName = "太郎",
            familyLastName = "山田",
            phoneNumber = "09012345678",
            iconImageUrl = "http://example.com/icon.png"
        )

        assertEquals(familyEmail.familyEmailID.value, familyEmail.familyEmailID.value)
        assertEquals("sample@sample.com", familyEmail.email.value)
        assertEquals("太郎", familyEmail.familyFirstName.value)
        assertEquals("山田", familyEmail.familyLastName.value)
        assertEquals("09012345678", familyEmail.phoneNumber.value)
        assertEquals("http://example.com/icon.png", familyEmail.iconImageUrl?.value)
    }

    @Test
    fun `should update family email`() {
        val familyEmail = FamilyEmail.create(
            familyEmailID = UUID.randomUUID(),
            email = "sample@sample.com",
            familyFirstName = "太郎",
            familyLastName = "山田",
            phoneNumber = "09012345678",
            iconImageUrl = "http://example.com/icon.png"
        )

        val updatedFamilyEmail = familyEmail.update(
            familyFirstName = FamilyFirstName("John"),
            familyLastName = FamilyLastName("Doe"),
            email = Email("john.doe@example.com"),
            phoneNumber = PhoneNumber("08087654321"),
            iconImageUrl = IconImageUrl("http://example.com/newicon.png")
        )
        assertEquals("John", updatedFamilyEmail.familyFirstName.value)
        assertEquals("Doe", updatedFamilyEmail.familyLastName.value)
        assertEquals("08087654321", updatedFamilyEmail.phoneNumber.value)
        assertEquals("http://example.com/newicon.png", updatedFamilyEmail.iconImageUrl?.value)
    }
}