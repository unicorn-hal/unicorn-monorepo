package com.unicorn.api.query_service.app_config

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

interface AppConfigQueryService {
    fun get(): AppConfigDto?
}

@Service
class AppConfigQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : AppConfigQueryService {
    override fun get(): AppConfigDto? {
        val sql =
            """
            SELECT 
                available,
                stun
            FROM app_config
            """.trimIndent()

        return namedParameterJdbcTemplate.query(sql) { rs, _ ->
            AppConfigDto(
                available = rs.getBoolean("available"),
                stunServerType = rs.getString("stun"),
            )
        }.singleOrNull()
    }
}

data class AppConfigDto(
    val available: Boolean,
    val stunServerType: String,
)
