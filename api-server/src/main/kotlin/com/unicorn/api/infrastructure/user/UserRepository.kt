package com.unicorn.api.infrastructure.user

import com.unicorn.api.domain.user.User
import com.unicorn.api.domain.user.UserID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate

interface UserRepository {
    fun store(user: User): User
    fun getOrNullBy(userID: UserID): User?
    fun delete(user: User): Unit
}

@Repository
class UserRepositoryImpl(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : UserRepository {
    override fun store(user: User): User {
        // language=postgresql
        val sql = """
            INSERT INTO users (
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
                occupation, 
                created_at
            ) VALUES (
                :userID, 
                :firstName, 
                :lastName,
                :gender::gender,
                :email,
                :birthDate,
                :address,
                :postalCode,
                :phoneNumber,
                :iconImageUrl,
                :bodyHeight,
                :bodyWeight,
                :occupation,
                NOW()
            ) 
            ON CONFLICT (user_id) 
            DO UPDATE SET
                first_name = EXCLUDED.first_name,
                last_name = EXCLUDED.last_name,
                gender = EXCLUDED.gender,
                email = EXCLUDED.email,
                birth_date = EXCLUDED.birth_date,
                address = EXCLUDED.address,
                postal_code = EXCLUDED.postal_code,
                phone_number = EXCLUDED.phone_number,
                icon_image_url = EXCLUDED.icon_image_url,
                body_height = EXCLUDED.body_height,
                body_weight = EXCLUDED.body_weight,
                occupation = EXCLUDED.occupation,
                deleted_at = NULL
            WHERE users.created_at IS NOT NULL;
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("userID", user.userID.value)
            .addValue("firstName", user.firstName.value)
            .addValue("lastName", user.lastName.value)
            .addValue("phoneNumber", user.phoneNumber.value)
            .addValue("email", user.email.value)
            .addValue("gender", user.gender.toString())
            .addValue("birthDate", user.birthDate.value)
            .addValue("address", user.address.value)
            .addValue("postalCode", user.postalCode.value)
            .addValue("iconImageUrl", user.iconImageUrl?.value)
            .addValue("bodyHeight", user.bodyHeight.value)
            .addValue("bodyWeight", user.bodyWeight.value)
            .addValue("occupation", user.occupation.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
        return user
    }

    override fun getOrNullBy(userID: UserID): User? {
        // language=postgresql
        val sql = """
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
        val sqlParams = MapSqlParameterSource()
            .addValue("userID", userID.value)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams
        ) { rs, _ ->
            User.fromStore(
                userID = rs.getString("user_id"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                gender = rs.getString("gender"),
                email = rs.getString("email"),
                birthDate = rs.getObject("birth_date", LocalDate::class.java),
                address = rs.getString("address"),
                postalCode = rs.getString("postal_code"),
                phoneNumber = rs.getString("phone_number"),
                iconImageUrl = rs.getString("icon_image_url"),
                bodyHeight = rs.getDouble("body_height"),
                bodyWeight = rs.getDouble("body_weight"),
                occupation = rs.getString("occupation")
            )
        }.singleOrNull()
    }

    override fun delete(user: User) {
        // language=postgresql
        val sql = """
            UPDATE users
            SET deleted_at = NOW()
            WHERE user_id = :userID
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("userID", user.userID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }

}