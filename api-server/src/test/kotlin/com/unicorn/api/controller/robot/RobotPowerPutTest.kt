package com.unicorn.api.controller.robot

import com.fasterxml.jackson.databind.ObjectMapper
import com.unicorn.api.domain.robot.Robot
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
class RobotPowerPutTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return 200 when robot is powered on`() {
        val robot =
            Robot.fromStore(
                robotID = "testtesttest",
                robotName = "robotName4",
                robotStatus = "shutdown",
            )
        val robotPowerRequest =
            RobotPowerRequest(
                status = "robot_waiting",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/robots/${robot.robotID.value}/power").headers(
                    HttpHeaders().apply {
                        add("X-UID", robot.robotID.value)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(robotPowerRequest)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                    {   
                        "robotID": "${robot.robotID.value}",
                        "robotName": "${robot.robotName.value}",
                        "robotStatus": "${robotPowerRequest.status}"
                    }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 200 when robot is powered off`() {
        val robot =
            Robot.fromStore(
                robotID = "test",
                robotName = "robotName",
                robotStatus = "robot_waiting",
            )

        val robotPowerRequest =
            RobotPowerRequest(
                status = "shutdown",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/robots/${robot.robotID.value}/power").headers(
                    HttpHeaders().apply {
                        add("X-UID", robot.robotID.value)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(robotPowerRequest)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                    {
                        "robotID": "${robot.robotID.value}",
                        "robotName": "${robot.robotName.value}",
                        "robotStatus": "${robotPowerRequest.status}"
                    }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when robot is supporting`() {
        val robotID = "testtest"
        val robotPowerRequest =
            RobotPowerRequest(
                status = "robot_waiting",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/robots/$robotID/power").headers(
                    HttpHeaders().apply {
                        add("X-UID", robotID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(robotPowerRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                    {
                        "errorType": "Robot is supporting"
                    }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when robot is already current status`() {
        val robotID = "testtesttest"
        val robotPowerRequest =
            RobotPowerRequest(
                status = "shutdown",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/robots/$robotID/power").headers(
                    HttpHeaders().apply {
                        add("X-UID", robotID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(robotPowerRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                    {
                        "errorType": "Robot status is already ${robotPowerRequest.status}"
                    }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when robot is not found`() {
        val robotID = "notfound"
        val robotPowerRequest =
            RobotPowerRequest(
                status = "robot_waiting",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/robots/$robotID/power").headers(
                    HttpHeaders().apply {
                        add("X-UID", robotID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(robotPowerRequest)),
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
