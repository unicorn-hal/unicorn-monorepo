package com.unicorn.api.query_service.family_email

import com.unicorn.api.domain.user.UserID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

interface FamilyEmailQueryService {
    fun get(userID: UserID): FamilyEmailResult
}

@Service
class FamilyEmailQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : FamilyEmailQueryService {
    override fun get(userID: UserID): FamilyEmailResult {
        //language=postgresql
        val sql =
            """
            SELECT
                family_email_id,
                email,
                family_first_name,
                family_last_name,
                icon_image_url
            FROM family_emails
            WHERE user_id = :userID 
            AND deleted_at IS NULL
            """.trimIndent()
        val sqlParams = MapSqlParameterSource().addValue("userID", userID.value)
        val familyEmails =
            namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                FamilyEmailDto(
                    familyEmailID = UUID.fromString(rs.getString("family_email_id")),
                    email = rs.getString("email"),
                    firstName = rs.getString("family_first_name"),
                    lastName = rs.getString("family_last_name"),
                    iconImageUrl = rs.getString("icon_image_url"),
                )
            }
        return FamilyEmailResult(familyEmails)
    }
}

data class FamilyEmailDto(
    val familyEmailID: UUID,
    val email: String,
    val firstName: String,
    val lastName: String,
    val iconImageUrl: String?,
)

data class FamilyEmailResult(
    val data: List<FamilyEmailDto>,
)
