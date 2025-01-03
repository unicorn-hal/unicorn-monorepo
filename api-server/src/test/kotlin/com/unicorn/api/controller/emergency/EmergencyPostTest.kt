package com.unicorn.api.controller.emergency

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/emergency/Insert_Emergency_Data.sql")
class EmergencyPostTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when emergency is created`() {
        val emergencyPostRequest =
            EmergencyPostRequest(
                userID = "test",
                userLatitude = 1.5,
                userLongitude = 1.5,
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/unicorn/emergency")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", "uid")
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(emergencyPostRequest)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "userID": "test",
                    "userLatitude": 1.5,
                    "userLongitude": 1.5
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 400 when user not found`() {
        val emergencyPostRequest =
            EmergencyPostRequest(
                userID = "notFound",
                userLatitude = 1.5,
                userLongitude = 1.5,
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/unicorn/emergency")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", "uid")
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(emergencyPostRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "User not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }
}
