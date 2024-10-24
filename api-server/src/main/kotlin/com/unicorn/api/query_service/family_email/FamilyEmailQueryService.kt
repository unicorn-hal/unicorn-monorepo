package com.unicorn.api.query_service.family_email

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

interface FamilyEmailQueryService {
    fun get(uid: String): FamilyEmailResult
}

@Service
class FamilyEmailQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : FamilyEmailQueryService {
    override fun get(uid: String): FamilyEmailResult {
        //language=postgresql
        val sql = """
            SELECT
                family_email_id,
                email,
                family_first_name,
                family_last_name,
                phone_number,
                icon_image_url
            FROM family_emails
            WHERE user_id = :userID 
            AND deleted_at IS NULL
        """.trimIndent()
        val sqlParams = MapSqlParameterSource().addValue("userID", uid)
        val familyEmails = namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            FamilyEmailDto(
                familyEmailID = UUID.fromString(rs.getString("family_email_id")),
                email = rs.getString("email"),
                familyFirstName = rs.getString("family_first_name"),
                familyLastName = rs.getString("family_last_name"),
                phoneNumber = rs.getString("phone_number"),
                iconImageUrl = rs.getString("icon_image_url")
            )
        }
        return FamilyEmailResult(familyEmails)
    }   
}

data class FamilyEmailDto(
    val familyEmailID: UUID,
    val email: String,
    val familyFirstName: String,
    val familyLastName: String,
    val phoneNumber: String,
    val iconImageUrl: String?
)
data class FamilyEmailResult(
    val data: List<FamilyEmailDto>
)