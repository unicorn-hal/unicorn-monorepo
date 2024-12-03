package com.unicorn.api.controller.robot

import com.fasterxml.jackson.databind.ObjectMapper
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

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/robot/Insert_Parent_Account_Data.sql")
@Sql("/db/robot/Insert_User_Account_Data.sql")
class RobotPostTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return 200 when robot is saved`() {
        val robotID = "robot"
        val saveRobotRequest =
            SaveRobotRequest(
                robotName = "robotName",
            )
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/robot").headers(
                    HttpHeaders().apply {
                        add("X-UID", robotID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(saveRobotRequest)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "robotName": "${saveRobotRequest.robotName}"
                }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when account is not found`() {
        val robotID = "invalid"
        val saveRobotRequest =
            SaveRobotRequest(
                robotName = "robotName",
            )
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/robot").headers(
                    HttpHeaders().apply {
                        add("X-UID", robotID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(saveRobotRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                    {
                        "errorType": "Account not found"
                    }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when account is not robot`() {
        val robotID = "user"
        val saveRobotRequest =
            SaveRobotRequest(
                robotName = "robotName",
            )
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/robot").headers(
                    HttpHeaders().apply {
                        add("X-UID", robotID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(saveRobotRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                    {
                        "errorType": "Account is not robot"
                    }
                """,
                true,
            ),
        )
    }
}
