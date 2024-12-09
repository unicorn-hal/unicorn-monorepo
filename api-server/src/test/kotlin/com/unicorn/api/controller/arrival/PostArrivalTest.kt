package com.unicorn.api.controller.arrival

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
class PostArrivalTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return 200 when arrival`() {
        val arrivalPostRequest =
            ArrivalPostRequest(
                userID = "test",
                robotLatitude = 35.6789,
                robotLongitude = 139.1234,
            )

        val robotID = "robot"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/unicorn/$robotID/arrival").headers(
                    HttpHeaders().apply {
                        add("X-UID", "robot")
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(arrivalPostRequest)),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    userID = "test",
                    robotLatitude = 35.6789,
                    robotLongitude = 139.1234
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when robot is not found`() {
        val arrivalPostRequest =
            ArrivalPostRequest(
                userID = "test",
                robotLatitude = 35.6789,
                robotLongitude = 139.1234,
            )

        val robotID = "notfound"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/unicorn/$robotID/arrival").headers(
                    HttpHeaders().apply {
                        add("X-UID", "robot")
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(arrivalPostRequest)),
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
    fun `should return 400 when user is not found`() {
        val arrivalPostRequest =
            ArrivalPostRequest(
                userID = "notfound",
                robotLatitude = 35.6789,
                robotLongitude = 139.1234,
            )

        val robotID = "robot"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/unicorn/$robotID/arrival").headers(
                    HttpHeaders().apply {
                        add("X-UID", "robot")
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(arrivalPostRequest)),
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
