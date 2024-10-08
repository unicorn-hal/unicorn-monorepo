package com.unicorn.api.controller.greeting

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.util.*

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GreetingPutTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val greetingID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")

    @Test
    fun `should return 200 when greeting is updated`() {
        val greetingRequest = GreetingPutRequest(
            message = "test"
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/greetings/${greetingID}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(greetingRequest)))

        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            {
                "message": "${greetingRequest.message}"
            }
        """.trimIndent()))
    }

    @Test
    fun `should return 500 when message is not provided`() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/greetings/${greetingID}")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "message": ""
                }
            """.trimIndent()))

        result.andExpect(status().isInternalServerError)
    }

    @Test
    fun `should return 500 when greeting is not found`() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/greetings/${UUID.randomUUID()}")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "message": "test"
                }
            """.trimIndent()))

        result.andExpect(status().isInternalServerError)
    }
}