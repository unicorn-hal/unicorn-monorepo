package com.unicorn.api.controller.greeting

import com.unicorn.api.query_service.greeting.GreetingDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GreetingGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when greeting is called`() {
        val greeting = GreetingDto(
            id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"),
            message = "Hello, World!"
        )
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/greetings/${greeting.id}"))


        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            {
                "id": "${greeting.id}",
                "message": "${greeting.message}"
            }
        """.trimIndent(), true))
    }

    @Test
    fun `should return 404 when greeting is not found`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/greetings/${UUID.randomUUID()}"))

        result.andExpect(status().isNotFound)
    }
}