package com.unicorn.api.query_service.account

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

interface AccountQueryService {
    fun getOrNullBy(uid: String): AccountDto?
}

@Service
class AccountQueryServiceImpl(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : AccountQueryService {
    override fun getOrNullBy(uid: String): AccountDto? {
        // language=postgresql
        val sql =
            """
            SELECT 
                uid,
                role,
                fcm_token_id
            FROM accounts
            WHERE uid = :uid 
                AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("uid", uid)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams,
        ) { rs, _ ->
            AccountDto(
                uid = rs.getString("uid"),
                role = rs.getString("role"),
                fcmTokenId = rs.getString("fcm_token_id"),
            )
        }.singleOrNull()
    }
}

data class AccountDto(
    val uid: String,
    val role: String,
    val fcmTokenId: String,
)
