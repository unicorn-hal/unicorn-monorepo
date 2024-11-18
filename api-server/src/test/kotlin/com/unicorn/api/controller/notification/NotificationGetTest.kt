package com.unicorn.api.controller.notification

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
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
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/notification/Insert_User_Data.sql")
@Sql("/db/user/Insert_Deleted_User_Data.sql")
@Sql("/db/notification/Insert_Notification_Data.sql")
class NotificationGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when get notification`() {
        val userID = "test"
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users/$userID/notification")
                    .headers(HttpHeaders().apply { add("X-UID", "uid") }),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "userID": "test",
                    "isMedicineReminder": true,
                    "isRegularHealthCheckup": true,
                    "isHospitalNews": true
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when user not found`() {
        val userID = "notfound"
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users/$userID/notification")
                    .headers(HttpHeaders().apply { add("X-UID", "uid") }),
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
    fun `should return 400 when user is deleted`() {
        val userID = "testtest"
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users/$userID/notification")
                    .headers(HttpHeaders().apply { add("X-UID", "uid") }),
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
    fun `should return 404 when notification not found`() {
        val userID = "12345"
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users/$userID/notification")
                    .headers(HttpHeaders().apply { add("X-UID", "uid") }),
            )
        result.andExpect(status().isNotFound)
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
