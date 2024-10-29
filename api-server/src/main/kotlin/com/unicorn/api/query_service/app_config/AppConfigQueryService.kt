package com.unicorn.api.query_service.app_config

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

interface AppConfigQueryService {
    fun get(): Boolean?
}

@Service
class AppConfigQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : AppConfigQueryService {

    override fun get(): Boolean? {
        val sql = """
            SELECT available
            FROM app_config
        """.trimIndent()

        return namedParameterJdbcTemplate.query(sql) { rs, _ ->
            rs.getBoolean("available")
        }.singleOrNull()
    }
}