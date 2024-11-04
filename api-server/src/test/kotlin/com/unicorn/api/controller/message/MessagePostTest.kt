package com.unicorn.api.controller.message

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
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
class MessagePostTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return 200 when post message`() {
        val chatID = "e38fd3d0-99bc-11ef-8e52-cfa170f7b603"
        val messagePostRequest =
            MessagePostRequest(
                senderID = "test",
                content = "test",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/chats/$chatID/messages")
                    .header("X-UID", "test")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(messagePostRequest)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "senderID": "test",
                    "content": "test"
                }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when post message with invalid request`() {
        val chatID = "e38fd3d0-99bc-11ef-8e52-cfa170f7b603"
        val messagePostRequest =
            MessagePostRequest(
                senderID = "test",
                content = "",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/chats/$chatID/messages")
                    .header("X-UID", "test")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(messagePostRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Content must not be blank"
                }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when senderID not found`() {
        val chatID = "e38fd3d0-99bc-11ef-8e52-cfa170f7b603"
        val messagePostRequest =
            MessagePostRequest(
                senderID = "not_found",
                content = "test",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/chats/$chatID/messages")
                    .header("X-UID", "test")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(messagePostRequest)),
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
    fun `should return 400 when chetID not found`() {
        val chatID = "e38fd3d0-99bc-11ef-8e52-cfa170f7b607"
        val messagePostRequest =
            MessagePostRequest(
                senderID = "test",
                content = "test",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/chats/$chatID/messages")
                    .header("X-UID", "test")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(messagePostRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Chat not found"
                }
                """,
                true,
            ),
        )
    }
}
