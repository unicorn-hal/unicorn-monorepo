package com.unicorn.api.infrastructure.greeting

import com.unicorn.api.domain.greeting.Greeting
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface GreetingRepository {
    fun store(greeting: Greeting): Greeting
    fun getOrNullById(id: UUID): Greeting?
    fun update(greeting: Greeting): Greeting
    fun delete(greeting: Greeting): Greeting
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

    override fun getOrNullById(id: UUID): Greeting? {
        val sql = """
            SELECT
                id,
                message
            FROM greeting
            WHERE id = :id
            AND deleted_at IS NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("id", id)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams
        ) { rs, _ ->
           Greeting.fromStore(
                id = rs.getObject("id", UUID::class.java),
                message = rs.getString("message")
           )
        }.firstOrNull()
    }

    override fun update(greeting: Greeting): Greeting {
        val sql = """
            UPDATE greeting
            SET message = :message
            WHERE id = :id
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("id", greeting.id)
            .addValue("message", greeting.message)

        namedParameterJdbcTemplate.update(sql, sqlParams)

        return greeting
    }

    override fun delete(greeting: Greeting): Greeting {
        val sql = """
            UPDATE greeting
            SET deleted_at = NOW()
            WHERE id = :id
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("id", greeting.id)

        namedParameterJdbcTemplate.update(sql, sqlParams)

        return greeting
    }
}