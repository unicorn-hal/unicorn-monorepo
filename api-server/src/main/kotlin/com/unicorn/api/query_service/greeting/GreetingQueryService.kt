package com.unicorn.api.query_service.greeting

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

interface GreetingQueryService {
    fun get(): GreetingResult
    fun getBy(id: UUID): GreetingDto?
}

@Service
class GreetingQueryServiceImpl(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):
    GreetingQueryService {
    override fun get(): GreetingResult {
        // language=SQL
        val sql = """
            SELECT
                id,
                message
            FROM greeting
            WHERE deleted_at IS NULL
        """.trimIndent()

        val greetings = namedParameterJdbcTemplate.query(
            sql,
            { rs, _ ->
                GreetingDto(
                    id = UUID.fromString(rs.getString("id")),
                    message = rs.getString("message")
                )
            }
        )

        return GreetingResult(greetings)
    }

    override fun getBy(id: UUID): GreetingDto? {
        // language=SQL
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
            GreetingDto(
                id = UUID.fromString(rs.getString("id")),
                message = rs.getString("message")
            )
        }.firstOrNull()
    }
}


data class GreetingDto(
    val id: UUID,
    val message: String
)

data class GreetingResult(
    val greetings: List<GreetingDto>
)



