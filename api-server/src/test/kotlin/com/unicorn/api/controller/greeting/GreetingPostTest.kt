package com.unicorn.api.controller.greeting

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GreetingPostTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when greeting is created`() {
        val greetingRequest =
            GreetingPostRequest(
                message = "Hello, World!",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/greetings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(greetingRequest)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "message": "${greetingRequest.message}"
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 500 when message is not provided`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/greetings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "message": ""
                        }
                        """.trimIndent(),
                    ),
            )

        result.andExpect(status().isInternalServerError)
    }
}
