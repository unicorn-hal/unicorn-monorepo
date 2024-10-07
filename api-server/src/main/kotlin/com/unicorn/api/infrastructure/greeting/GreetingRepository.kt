package com.unicorn.api.infrastructure.greeting

import com.unicorn.api.domain.greeting.Greeting
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

interface GreetingRepository {
    fun store(greeting: Greeting): Greeting
}

@Repository
class GreetingRepositoryImpl(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : GreetingRepository {
    override fun store(greeting: Greeting): Greeting {
        val sql = """
            INSERT INTO greeting (id, message)
            VALUES (:id, :message)
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("id", greeting.id)
            .addValue("message", greeting.message)

        namedParameterJdbcTemplate.update(sql, sqlParams)

        return greeting
    }
}