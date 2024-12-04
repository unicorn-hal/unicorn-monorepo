package com.unicorn.api.controller.robot

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
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
class RobotGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Nested
    inner class GetAllRobots {
        @Test
        fun `should return 200 when robots are found`() {
            val result =
                mockMvc.perform(
                    MockMvcRequestBuilders.get("/robots").headers(
                        HttpHeaders().apply {
                            add("X-UID", "test")
                        },
                    ),
                )

            result.andExpect(status().isOk)
            result.andExpect(
                content().json(
                    """
                    {
                        "data": [
                            {
                                "robotID": "test",
                                "robotName": "robotName"
                            },
                            {
                                "robotID": "testtest",
                                "robotName": "robotName3"
                            }
                        ]
                    }
                    """,
                ),
            )
        }
    }

    @Nested
    inner class GetRobot {
        @Test
        fun `should return 200 when robot is found`() {
            val robotID = "test"
            val result =
                mockMvc.perform(
                    MockMvcRequestBuilders.get("/robots/$robotID").headers(
                        HttpHeaders().apply {
                            add("X-UID", "test")
                        },
                    ),
                )

            result.andExpect(status().isOk)
            result.andExpect(
                content().json(
                    """
                    {
                        "robotID": "test",
                        "robotName": "robotName"
                    }
                    """,
                ),
            )
        }

        @Test
        fun `should return 400 when robot is not found`() {
            val robotID = "notFound"
            val result =
                mockMvc.perform(
                    MockMvcRequestBuilders.get("/robots/$robotID").headers(
                        HttpHeaders().apply {
                            add("X-UID", "test")
                        },
                    ),
                )

            result.andExpect(status().isBadRequest)
            result.andExpect(
                content().json(
                    """
                    {
                        "errorType": "Robot not found"
                    }
                    """,
                ),
            )
        }
    }
}
