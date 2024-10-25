 package com.unicorn.api.infrastructure.family_email

import com.unicorn.api.domain.family_email.*
import com.unicorn.api.domain.user.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import org.junit.jupiter.api.Assertions.*
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/family_email/Insert_FamilyEmail_Data.sql")

class FamilyEmailRepositoryTest {
    @Autowired
    private lateinit var FamilyEmailRepository: FamilyEmailRepository
    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findFamilyEmailByID(familyEmailID: UUID): FamilyEmail? {
        //language=postgresql
        val sql = """
            SELECT
                family_email_id,
                user_id,
                email,
                family_first_name,
                family_last_name,
                phone_number,
                icon_image_url
            FROM family_emails
            WHERE family_email_id = :familyEmailID
            AND deleted_at IS NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource().addValue("familyEmailID", familyEmailID)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            FamilyEmail.fromStore(
                familyEmailID = UUID.fromString(rs.getString("family_email_id")),
                email = rs.getString("email"),
                firstName = rs.getString("family_first_name"),
                lastName = rs.getString("family_last_name"),
                phoneNumber = rs.getString("phone_number"),
                iconImageUrl = rs.getString("icon_image_url")
            )
        }.singleOrNull()
    }

    @Test
    fun `should store family email`() {
        val familyEmail = FamilyEmail.create(
            familyEmailID = UUID.randomUUID(),
            email = "test2@example.com",
            firstName = "test",
            lastName = "test",
            phoneNumber = "07012345678",
            iconImageUrl = "http://example.com/icon.png"
        )
        val userID = UserID("test")
        FamilyEmailRepository.store(familyEmail, userID)
        val storedFamilyEmail = findFamilyEmailByID(familyEmail.familyEmailID.value)
        assertEquals(familyEmail.familyEmailID, storedFamilyEmail?.familyEmailID)
        assertEquals(familyEmail.email, storedFamilyEmail?.email)
        assertEquals(familyEmail.firstName, storedFamilyEmail?.firstName)
        assertEquals(familyEmail.lastName, storedFamilyEmail?.lastName)
        assertEquals(familyEmail.phoneNumber, storedFamilyEmail?.phoneNumber)
        assertEquals(familyEmail.iconImageUrl, storedFamilyEmail?.iconImageUrl)
    }
    @Test
    fun `should update family email`() {
        val familyEmail = FamilyEmail.create(
            familyEmailID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470"),
            email = "test@test.com",
            firstName = "test",
            lastName = "test",
            phoneNumber = "07012345678",
            iconImageUrl = "http://example.com/icon.png"
        )
        val userID = UserID("test")
        FamilyEmailRepository.store(familyEmail, userID)

        val result = findFamilyEmailByID(familyEmail.familyEmailID.value)
        assertEquals(familyEmail.familyEmailID, result?.familyEmailID)
        assertEquals(familyEmail.email, result?.email)
        assertEquals(familyEmail.firstName, result?.firstName)
        assertEquals(familyEmail.lastName, result?.lastName)
        assertEquals(familyEmail.phoneNumber, result?.phoneNumber)
        assertEquals(familyEmail.iconImageUrl, result?.iconImageUrl)
    }

    @Test
    fun `should find family email by familyEmailID`() {
        val familyEmailID = FamilyEmailID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470"))
        val foundFamilyEmail = FamilyEmailRepository.getOrNullBy(familyEmailID)
        assertNotNull(foundFamilyEmail)
        assertEquals(familyEmailID, foundFamilyEmail!!.familyEmailID)
        assertEquals("sample@sample.com", foundFamilyEmail.email.value)
        assertEquals("太郎", foundFamilyEmail.firstName.value)
        assertEquals("山田", foundFamilyEmail.lastName.value)
        assertEquals("09012345678", foundFamilyEmail.phoneNumber.value)
        assertEquals("https://example.com", foundFamilyEmail.iconImageUrl?.value)
    }

    @Test
    fun `Should not be found if deleted_at is not NULL`() {
        val familyEmailID = FamilyEmailID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d471"))
        val foundFamilyEmail = FamilyEmailRepository.getOrNullBy(familyEmailID)
        assertNull(foundFamilyEmail)
    }

    @Test
    fun ` should return null when family email does not exist`() {
        val familyEmailID = FamilyEmailID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d472"))
        val foundFamilyEmail = FamilyEmailRepository.getOrNullBy(familyEmailID)
        assertNull(foundFamilyEmail)
    }

    @Test
    fun `should delete family email`() {
        val familyEmail = FamilyEmail.create(
            familyEmailID = UUID.randomUUID(),
            email = "sample@sample.com",
            firstName = "太郎",
            lastName = "山田",
            phoneNumber = "09012345678",
            iconImageUrl = "http://example.com/icon.png"
        )
        val userID = UserID("test")
        FamilyEmailRepository.store(familyEmail, userID)
        FamilyEmailRepository.delete(familyEmail)
        val deletedFamilyEmail = FamilyEmailRepository.getOrNullBy(familyEmail.familyEmailID)
        assertNull(deletedFamilyEmail)
    }
}