package com.unicorn.api.query_service.user

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.time.LocalDate

interface UserQueryService {
    fun getOrNullBy(userID: String): UserDto?
}

@Service
class UserQueryServiceImpl(
    val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : UserQueryService {
    override fun getOrNullBy(userID: String): UserDto? {
        // language=postgresql
        val sql =
            """
            SELECT
                user_id, 
                first_name, 
                last_name, 
                gender, 
                email, 
                birth_date, 
                address, 
                postal_code, 
                phone_number, 
                icon_image_url, 
                body_height, 
                body_weight, 
                occupation
            FROM users
            WHERE user_id = :userID
                AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", userID)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams,
        ) { rs, _ ->
            UserDto(
                userID = rs.getString("user_id"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                email = rs.getString("email"),
                birthDate = rs.getObject("birth_date", LocalDate::class.java),
                gender = rs.getString("gender"),
                address = rs.getString("address"),
                postalCode = rs.getString("postal_code"),
                phoneNumber = rs.getString("phone_number"),
                iconImageUrl = rs.getString("icon_image_url"),
                bodyHeight = rs.getDouble("body_height"),
                bodyWeight = rs.getDouble("body_weight"),
                occupation = rs.getString("occupation"),
            )
        }.singleOrNull()
    }
}

data class UserDto(
    val userID: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthDate: LocalDate,
    val gender: String,
    val address: String,
    val postalCode: String,
    val phoneNumber: String,
    val iconImageUrl: String?,
    val bodyHeight: Double,
    val bodyWeight: Double,
    val occupation: String,
)
