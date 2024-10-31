package com.unicorn.api.infrastructure.greeting

import com.unicorn.api.domain.greeting.Greeting
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/greeting/Insert_Greeting_Data.sql")
class GreetingRepositoryTest {
    @Autowired
    private lateinit var greetingRepository: GreetingRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    companion object {
        private val greeting =
            Greeting(
                id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"),
                message = "Hello, World!",
            )
    }

    // RowMapper to map the result set to a Greeting object
    private val rowMapper =
        RowMapper { rs: ResultSet, _: Int ->
            Greeting(
                id = UUID.fromString(rs.getString("id")),
                message = rs.getString("message"),
            )
        }

    @Test
    fun `should store greeting`() {
        // Arrange: Create a Greeting object with a random UUID
        val greeting =
            Greeting(
                id = UUID.randomUUID(),
                message = "Hello, World!",
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

    @Test
    fun `should get greeting by id`() {
        // Assert: Verify that the greeting can be retrieved by its ID
        val retrievedGreeting = greetingRepository.getOrNullById(greeting.id)

        assertEquals(greeting.id, retrievedGreeting?.id)
        assertEquals(greeting.message, retrievedGreeting?.message)
    }

    @Test
    fun `should update greeting`() {
        val updatedGreeting = greeting.copy(message = "Hello, Universe!")

        // Act: Update the greeting using the repository
        greetingRepository.update(updatedGreeting)

        // Assert: Verify that the greeting has been updated correctly
        val sql = "SELECT id, message FROM greeting WHERE id = :id"
        val sqlParams = MapSqlParameterSource().addValue("id", updatedGreeting.id)
        val storedGreeting = namedParameterJdbcTemplate.queryForObject(sql, sqlParams, rowMapper)

        assertEquals(updatedGreeting.id, storedGreeting?.id)
        assertEquals(updatedGreeting.message, storedGreeting?.message)
    }
}
