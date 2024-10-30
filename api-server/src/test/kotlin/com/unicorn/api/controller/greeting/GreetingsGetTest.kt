package com.unicorn.api.controller.greeting

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
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
class GreetingsGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when greeting is called`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/greetings").headers(
                    HttpHeaders().apply {
                        add("X-UID", "f47ac10b-58cc-4372-a567-0e02b2c3d479")
                    },
                ),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "greetings": [
                        {
                            "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                            "message": "Hello, World!"
                        }
                    ]
                }
                """.trimIndent(),
                true,
            ),
        )
    }
}
