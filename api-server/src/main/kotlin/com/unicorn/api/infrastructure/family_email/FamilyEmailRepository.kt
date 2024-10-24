package com.unicorn.api.infrastructure.family_email

import com.unicorn.api.domain.user.UserID
import com.unicorn.api.domain.family_email.*
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

interface FamilyEmailRepository {
    fun store(familyEmail: FamilyEmail, userID: UserID): FamilyEmail
    fun getOrNullBy(familyEmailID: FamilyEmailID): FamilyEmail?
    fun delete(familyEmail: FamilyEmail): Unit
}

@Repository
class FamilyEmailRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : FamilyEmailRepository {

    override fun store(familyEmail: FamilyEmail, userID: UserID): FamilyEmail {
        // language=postgresql
        val sql = """
            INSERT INTO family_emails (
                family_email_id,
                user_id,
                email,
                family_first_name,
                family_last_name,
                phone_number,
                icon_image_url,
                created_at
            ) VALUES (
                :familyEmailID,
                :userID,
                :email,
                :familyFirstName,
                :familyLastName,
                :phoneNumber,
                :iconImageUrl,
                NOW()
            )
            ON CONFLICT (family_email_id)
            DO UPDATE SET
                email = EXCLUDED.email,
                family_first_name = EXCLUDED.family_first_name,
                family_last_name = EXCLUDED.family_last_name,
                phone_number = EXCLUDED.phone_number,
                icon_image_url = EXCLUDED.icon_image_url,
                deleted_at = NULL
            WHERE family_emails.created_at IS NOT NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("familyEmailID", familyEmail.familyEmailID.value)
            .addValue("userID", userID.value)
            .addValue("email", familyEmail.email.value)
            .addValue("familyFirstName", familyEmail.familyFirstName.value)
            .addValue("familyLastName", familyEmail.familyLastName.value)
            .addValue("phoneNumber", familyEmail.phoneNumber.value)
            .addValue("iconImageUrl", familyEmail.iconImageUrl?.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)

        return familyEmail
    }

    override fun getOrNullBy(familyEmailID: FamilyEmailID): FamilyEmail? {
        // language=postgresql
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
        val sqlParams = MapSqlParameterSource()
            .addValue("familyEmailID", familyEmailID.value)
        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams
        ) { rs, _ ->
            FamilyEmail.fromStore(
                familyEmailID = UUID.fromString(rs.getString("family_email_id")),
                email = rs.getString("email"),
                familyFirstName = rs.getString("family_first_name"),
                familyLastName = rs.getString("family_last_name"),
                phoneNumber = rs.getString("phone_number"),
                iconImageUrl = rs.getString("icon_image_url")
            )
        }.singleOrNull()
    }

    override fun delete(familyEmail: FamilyEmail) {
        // language=postgresql
        val sql = """
            UPDATE family_emails
            SET deleted_at = NOW()
            WHERE family_email_id = :familyEmailID
        """.trimIndent()
        val sqlParams = MapSqlParameterSource()
            .addValue("familyEmailID", familyEmail.familyEmailID.value)
        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}