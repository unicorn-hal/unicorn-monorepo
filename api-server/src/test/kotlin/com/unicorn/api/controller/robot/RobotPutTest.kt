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
@Sql("/db/robot/Insert_Robot_Data.sql")
class RobotPutTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return 200 when robot is saved`() {
        val robotID = "test"
        val updateRobotRequest =
            UpdateRobotRequest(
                robotName = "updateRobotName",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/robot/$robotID").headers(
                    HttpHeaders().apply {
                        add("X-UID", robotID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRobotRequest)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "robotName": "${updateRobotRequest.robotName}"
                }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when robot name is empty`() {
        val robotID = "test"
        val updateRobotRequest =
            UpdateRobotRequest(
                robotName = "",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/robot/$robotID").headers(
                    HttpHeaders().apply {
                        add("X-UID", robotID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRobotRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Robot name should not be blank"
                }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when robotID is invalid`() {
        val robotID = "invalid"
        val updateRobotRequest =
            UpdateRobotRequest(
                robotName = "updateRobotName",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/robot/$robotID").headers(
                    HttpHeaders().apply {
                        add("X-UID", robotID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRobotRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Robot not found"
                }
                """,
                true,
            ),
        )
    }
}
