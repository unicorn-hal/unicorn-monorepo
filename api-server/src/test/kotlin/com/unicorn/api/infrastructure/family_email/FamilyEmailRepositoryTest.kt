package com.unicorn.api.infrastructure.family_email

import com.unicorn.api.domain.family_email.*
import com.unicorn.api.domain.user.User
import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
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
    private lateinit var familyEmailRepository: FamilyEmailRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findFamilyEmailByID(familyEmailID: UUID): FamilyEmail? {
        //language=postgresql
        val sql =
            """
            SELECT
                family_email_id,
                user_id,
                email,
                family_first_name,
                family_last_name,
                icon_image_url
            FROM family_emails
            WHERE family_email_id = :familyEmailID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams = MapSqlParameterSource().addValue("familyEmailID", familyEmailID)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            FamilyEmail.fromStore(
                familyEmailID = UUID.fromString(rs.getString("family_email_id")),
                userID = rs.getString("user_id"),
                email = rs.getString("email"),
                firstName = rs.getString("family_first_name"),
                lastName = rs.getString("family_last_name"),
                iconImageUrl = rs.getString("icon_image_url"),
            )
        }.singleOrNull()
    }

    private fun findFamilyEmailByUserID(userID: UserID): List<FamilyEmail> {
        //language=postgresql
        val sql =
            """
            SELECT
                family_email_id,
                user_id,
                email,
                family_first_name,
                family_last_name,
                icon_image_url
            FROM family_emails
            WHERE user_id = :userID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams = MapSqlParameterSource().addValue("userID", userID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            FamilyEmail.fromStore(
                familyEmailID = UUID.fromString(rs.getString("family_email_id")),
                userID = rs.getString("user_id"),
                email = rs.getString("email"),
                firstName = rs.getString("family_first_name"),
                lastName = rs.getString("family_last_name"),
                iconImageUrl = rs.getString("icon_image_url"),
            )
        }
    }

    @Test
    fun `should store family email`() {
        val familyEmail =
            FamilyEmail.create(
                familyEmailID = UUID.randomUUID(),
                userID = "test",
                email = "test2@example.com",
                firstName = "test",
                lastName = "test",
                iconImageUrl = "http://example.com/icon.png",
            )

        familyEmailRepository.store(familyEmail)
        val storedFamilyEmail = findFamilyEmailByID(familyEmail.familyEmailID.value)
        assertEquals(familyEmail.userID, storedFamilyEmail?.userID)
        assertEquals(familyEmail.email, storedFamilyEmail?.email)
        assertEquals(familyEmail.firstName, storedFamilyEmail?.firstName)
        assertEquals(familyEmail.lastName, storedFamilyEmail?.lastName)
        assertEquals(familyEmail.iconImageUrl, storedFamilyEmail?.iconImageUrl)
    }

    @Test
    fun `should update family email`() {
        val familyEmail =
            FamilyEmail.fromStore(
                familyEmailID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470"),
                userID = "test",
                email = "sample@sample.com",
                firstName = "太郎",
                lastName = "山田",
                iconImageUrl = "https://example.com",
            )
        val updatedFamilyEmail =
            familyEmail.update(
                email = Email("test@example.com"),
                firstName = FirstName("John"),
                lastName = LastName("Doe"),
                iconImageUrl = IconImageUrl("http://example.com/newicon.png"),
            )
        familyEmailRepository.store(updatedFamilyEmail)

        val result = findFamilyEmailByID(familyEmail.familyEmailID.value)
        assertEquals(familyEmail.familyEmailID, result?.familyEmailID)
        assertEquals(familyEmail.userID, result?.userID)
        assertEquals(updatedFamilyEmail.email, result?.email)
        assertEquals(updatedFamilyEmail.firstName, result?.firstName)
        assertEquals(updatedFamilyEmail.lastName, result?.lastName)
        assertEquals(updatedFamilyEmail.iconImageUrl, result?.iconImageUrl)
    }

    @Test
    fun `should find family email by familyEmailID`() {
        val familyEmailID = FamilyEmailID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470"))
        val foundFamilyEmail = familyEmailRepository.getOrNullBy(familyEmailID)
        assertNotNull(foundFamilyEmail)
        assertEquals(familyEmailID, foundFamilyEmail!!.familyEmailID)
        assertEquals("sample@sample.com", foundFamilyEmail.email.value)
        assertEquals("太郎", foundFamilyEmail.firstName.value)
        assertEquals("山田", foundFamilyEmail.lastName.value)
        assertEquals("https://example.com", foundFamilyEmail.iconImageUrl?.value)
    }

    @Test
    fun `Should not be found if deleted_at is not NULL`() {
        val familyEmailID = FamilyEmailID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d471"))
        val foundFamilyEmail = familyEmailRepository.getOrNullBy(familyEmailID)
        assertNull(foundFamilyEmail)
    }

    @Test
    fun ` should return null when family email does not exist`() {
        val familyEmailID = FamilyEmailID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d472"))
        val foundFamilyEmail = familyEmailRepository.getOrNullBy(familyEmailID)
        assertNull(foundFamilyEmail)
    }

    @Test
    fun `should delete family email`() {
        val familyEmail =
            FamilyEmail.create(
                familyEmailID = UUID.randomUUID(),
                userID = "test",
                email = "sample@sample.com",
                firstName = "太郎",
                lastName = "山田",
                iconImageUrl = "http://example.com/icon.png",
            )
        familyEmailRepository.store(familyEmail)
        familyEmailRepository.delete(familyEmail)
        val deletedFamilyEmail = familyEmailRepository.getOrNullBy(familyEmail.familyEmailID)
        assertNull(deletedFamilyEmail)
    }

    @Test
    fun `should find family emails by userID`() {
        val userID = UserID("test")

        val familyEmails = familyEmailRepository.getOrNullByUserID(userID)

        assertNotNull(familyEmails)
        assertEquals(1, familyEmails.size)
        assertEquals("sample@sample.com", familyEmails[0].email.value)
        assertEquals("太郎", familyEmails[0].firstName.value)
        assertEquals("山田", familyEmails[0].lastName.value)
        assertEquals("https://example.com", familyEmails[0].iconImageUrl?.value)
    }

    @Test
    fun `should not find family emails by not found userID`() {
        val userID = UserID("notFound")

        val familyEmails = familyEmailRepository.getOrNullByUserID(userID)

        assertEquals(0, familyEmails.size)
    }

    @Test
    fun `should delete family email by user`() {
        val user =
            User.fromStore(
                userID = "test",
                firstName = "test",
                lastName = "test",
                email = "sample@test.com",
                birthDate = LocalDate.of(1990, 1, 1),
                gender = "male",
                address = "test",
                postalCode = "0000000",
                phoneNumber = "00000000000",
                iconImageUrl = "https://example.com",
                bodyHeight = 170.4,
                bodyWeight = 60.4,
                occupation = "test",
            )

        familyEmailRepository.deleteByUser(user)

        val deletedFamilyEmail = findFamilyEmailByUserID(user.userID)
        assertEquals(0, deletedFamilyEmail.size)
    }
}
