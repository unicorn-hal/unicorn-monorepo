package com.unicorn.api.controller.robot

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
class RobotDeleteTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 204 when robot is deleted`() {
        val robotID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/robots/$robotID").headers(
                    HttpHeaders().apply {
                        add("X-UID", robotID)
                    },
                ).contentType(MediaType.APPLICATION_JSON),
            )

        result.andExpect(status().isNoContent)
    }

    @Test
    fun `should return 400 when robot is not found`() {
        val robotID = "notFound"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/robots/$robotID").headers(
                    HttpHeaders().apply {
                        add("X-UID", robotID)
                    },
                ).contentType(MediaType.APPLICATION_JSON),
            )

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when robot is already deleted`() {
        val robotID = "12345"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/robots/$robotID").headers(
                    HttpHeaders().apply {
                        add("X-UID", robotID)
                    },
                ).contentType(MediaType.APPLICATION_JSON),
            )

        result.andExpect(status().isBadRequest)
    }
}
