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
@Sql("/db/notification/Insert_User_Data.sql")
@Sql("/db/notification/Insert_Notification_Data.sql")
class NotificationPutTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return 200 when update notification`() {
        val notification =
            NotificationPutRequest(
                isMedicineReminder = false,
                isRegularHealthCheckup = false,
                isHospitalNews = false,
            )
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/users/$userID/notification")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", "uid")
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(notification)),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "isMedicineReminder": ${notification.isMedicineReminder},
                    "isRegularHealthCheckup": ${notification.isRegularHealthCheckup},
                    "isHospitalNews": ${notification.isHospitalNews}
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when userID is deleted`() {
        val notification =
            NotificationPutRequest(
                isMedicineReminder = false,
                isRegularHealthCheckup = false,
                isHospitalNews = false,
            )
        val userID = "testtest"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/users/$userID/notification")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", "uid")
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(notification)),
            )
        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "errorType": "User not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when userID is not found`() {
        val notification =
            NotificationPutRequest(
                isMedicineReminder = false,
                isRegularHealthCheckup = false,
                isHospitalNews = false,
            )
        val userID = "notfound"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/users/$userID/notification")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", "uid")
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(notification)),
            )
        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "errorType": "User not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when notification is not found`() {
        val notification =
            NotificationPutRequest(
                isMedicineReminder = false,
                isRegularHealthCheckup = false,
                isHospitalNews = false,
            )
        val userID = "12345"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/users/$userID/notification")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", "uid")
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(notification)),
            )
        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "errorType": "Notification not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }
}
