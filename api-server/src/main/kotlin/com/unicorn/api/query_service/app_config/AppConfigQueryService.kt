package com.unicorn.api.query_service.app_config

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

interface AppConfigQueryService {
    fun get(): AppConfigResponse
}

@Service
class AppConfigQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : AppConfigQueryService {

    override fun get(): AppConfigResponse {
        val sql = """
            SELECT available
            FROM app_config
        """.trimIndent()

        val results = namedParameterJdbcTemplate.query(sql) { rs, _ ->
            rs.getBoolean("available")
        }

        return when (results.size) {
            1 -> AppConfigResponse(available = results[0])
            else -> AppConfigResponse(errorType = "serverError")
        }
    }
}

data class AppConfigResponse(
    val available: Boolean? = null,
    val errorType: String? = null
)
