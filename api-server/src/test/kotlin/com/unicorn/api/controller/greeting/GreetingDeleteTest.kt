package com.unicorn.api.controller.greeting

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/db/greeting/Insert_Greeting_Data.sql")
class GreetingDeleteTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    companion object {
        private val deleteGreetingID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d480")
    }

    @Test
    fun `should return 200 when greeting is deleted`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.delete("/greetings/${deleteGreetingID}"))

        result.andExpect(status().isOk)
    }

    @Test
    fun `should return 500 when greeting is not found`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.delete("/greetings/${UUID.randomUUID()}"))

        result.andExpect(status().isInternalServerError)
    }
}