package com.unicorn.api.controller.complete

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
@Sql("/db/robot_support/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/robot_support/Insert_Robot_Data.sql")
@Sql("/db/emergency/Insert_Emergency_Data.sql")
@Sql("/db/robot_support/Insert_Robot_Support_Data.sql")
class PostCompleteTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when complete`() {
        val completePostRequest =
            CompletePostRequest(
                robotSupportID = UUID.fromString("c64e788c-dd0a-72d8-a271-5460e1f29816"),
                userID = "test",
            )

        val robotID = "robot"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/unicorn/$robotID/complete").headers(
                    HttpHeaders().apply {
                        add("X-UID", "robot")
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(completePostRequest)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "robotID": "robot",
                    "robotName": "robotName",
                    "status": "complete",
                    "waitingNumber": null,
                    "robotLatitude": null,
                    "robotLongitude": null
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when robot is not found`() {
        val completePostRequest =
            CompletePostRequest(
                robotSupportID = UUID.fromString("c64e788c-dd0a-72d8-a271-5460e1f29816"),
                userID = "test",
            )

        val robotID = "notFound"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/unicorn/$robotID/complete").headers(
                    HttpHeaders().apply {
                        add("X-UID", "robot")
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(completePostRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Robot not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when robot support is not found`() {
        val completePostRequest =
            CompletePostRequest(
                robotSupportID = UUID.fromString("c64e788c-dd0a-72d8-a271-5460e1f29800"),
                userID = "test",
            )

        val robotID = "robot"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/unicorn/$robotID/complete").headers(
                    HttpHeaders().apply {
                        add("X-UID", "robot")
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(completePostRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Robot support not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when user is not found`() {
        val completePostRequest =
            CompletePostRequest(
                robotSupportID = UUID.fromString("c64e788c-dd0a-72d8-a271-5460e1f29816"),
                userID = "notFound",
            )

        val robotID = "robot"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/unicorn/$robotID/complete").headers(
                    HttpHeaders().apply {
                        add("X-UID", "robot")
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(completePostRequest)),
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
