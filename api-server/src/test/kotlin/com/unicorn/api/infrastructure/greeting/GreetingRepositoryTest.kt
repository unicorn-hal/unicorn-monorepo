package com.unicorn.api.infrastructure.greeting

import com.unicorn.api.domain.greeting.Greeting
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.sql.ResultSet
import java.util.*

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class GreetingRepositoryTest {

    @Autowired
    private lateinit var greetingRepository: GreetingRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    // RowMapper to map the result set to a Greeting object
    private val rowMapper = RowMapper { rs: ResultSet, _: Int ->
        Greeting(
            id = UUID.fromString(rs.getString("id")),
            message = rs.getString("message")
        )
    }

    @Test
    fun `should store greeting`() {
        // Arrange: Create a Greeting object with a random UUID
        val greeting = Greeting(
            id = UUID.randomUUID(),
            message = "Hello, World!"
        )

        // Act: Store the greeting using the repository
        greetingRepository.store(greeting)

        // Assert: Verify that the greeting has been stored correctly
        val sql = "SELECT id, message FROM greeting WHERE id = :id"
        val sqlParams = MapSqlParameterSource().addValue("id", greeting.id)
        val storedGreeting = namedParameterJdbcTemplate.queryForObject(sql, sqlParams, rowMapper)

        assertEquals(greeting.id, storedGreeting?.id)
        assertEquals(greeting.message, storedGreeting?.message)
    }

}
