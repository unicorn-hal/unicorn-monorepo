package com.unicorn.api.controller.message

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
@Sql("/db/message/Insert_Message_Data.sql")
class MessageDeleteTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 204 when delete message`() {
        val chatID = "e38fd3d0-99bc-11ef-8e52-cfa170f7b603"
        val messageID = "66197db0-99bd-11ef-8e52-cfa170f7b603"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/chats/$chatID/messages/$messageID")
                    .header("X-UID", "test"),
            )

        result.andExpect(status().isNoContent)
    }

    @Test
    fun `should return 400 when delete message with invalid chatID`() {
        val chatID = "00000000-0000-0000-0000-000000000000"
        val messageID = "66197db0-99bd-11ef-8e52-cfa170f7b603"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/chats/$chatID/messages/$messageID")
                    .header("X-UID", "test"),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Chat not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when delete message with invalid messageID`() {
        val chatID = "e38fd3d0-99bc-11ef-8e52-cfa170f7b603"
        val messageID = "00000000-0000-0000-0000-000000000000"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/chats/$chatID/messages/$messageID")
                    .header("X-UID", "test"),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Message not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when delete message with invalid uid`() {
        val chatID = "e38fd3d0-99bc-11ef-8e52-cfa170f7b603"
        val messageID = "66197db0-99bd-11ef-8e52-cfa170f7b603"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/chats/$chatID/messages/$messageID")
                    .header("X-UID", "invalid"),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Account not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when delete message with invalid sender`() {
        val chatID = "e38fd3d0-99bc-11ef-8e52-cfa170f7b603"
        val messageID = "66197db0-99bd-11ef-8e52-cfa170f7b603"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/chats/$chatID/messages/$messageID")
                    .header("X-UID", "doctor"),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "You are not the sender of this message"
                }
                """.trimIndent(),
                true,
            ),
        )
    }
}
