package com.unicorn.api.controller.notification

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/notification/Insert_User_Data.sql")
@Sql("/db/notification/Insert_Notification_Data.sql")
class NotificationDeleteTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return 204 when delete notification`() {
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/$userID/notification")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isNoContent)
    }

    @Test
    fun `should return 400 when user not found`() {
        val userID = "not_found"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/$userID/notification")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when notification not found`() {
        val userID = "12345"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/$userID/notification")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isBadRequest)
    }
}
