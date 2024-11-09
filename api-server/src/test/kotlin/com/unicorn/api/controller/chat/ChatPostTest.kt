package com.unicorn.api.controller.chat

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@AutoConfigureMockMvc
@Sql("/db/account/Insert_Account_Data.sql")
@Sql("/db/hospital/Insert_Hospital_Data.sql")
@Sql("/db/department/Insert_Department_Data.sql")
@Sql("/db/doctor/Insert_Doctor_Data.sql")
@Sql("/db/doctor_department/Insert_Doctor_Department_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/chat/Insert_Chat_Data.sql")
class ChatPostTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return 200 when post chat`() {
        val chatPostRequest =
            ChatPostRequest(
                doctorID = "doctor",
                userID = "test",
            )

        val result =
            mockMvc.perform(
                post("/chats")
                    .header("X-UID", "test")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(chatPostRequest)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "doctorID": "doctor",
                    "userID": "test"
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 400 when user not found`() {
        val chatPostRequest =
            ChatPostRequest(
                doctorID = "doctor",
                userID = "not_found",
            )

        val result =
            mockMvc.perform(
                post("/chats")
                    .header("X-UID", "test")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(chatPostRequest)),
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

    @Test
    fun `should return 400 when doctor not found`() {
        val chatPostRequest =
            ChatPostRequest(
                doctorID = "not_found",
                userID = "test",
            )

        val result =
            mockMvc.perform(
                post("/chats")
                    .header("X-UID", "test")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(chatPostRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Doctor not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }
}
