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
class MessageGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when get messages`() {
        val chatID = "e38fd3d0-99bc-11ef-8e52-cfa170f7b603"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/chats/$chatID/messages")
                    .header("X-UID", "test"),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": [
                        {
                            "messageID": "66197db0-99bd-11ef-8e52-cfa170f7b603",
                            "chatID": "e38fd3d0-99bc-11ef-8e52-cfa170f7b603",
                            "senderID": "test",
                            "firstName": "test",
                            "lastName": "test",
                            "iconImageUrl": "https://example.com",
                            "content": "Hello",
                            "sentAt": "2021-04-01T19:00:00+09:00"
                        },
                        {
                            "messageID": "66197db1-99bd-11ef-8e52-cfa170f7b604",
                            "chatID": "e38fd3d0-99bc-11ef-8e52-cfa170f7b603",
                            "senderID": "doctor",
                            "firstName": "test",
                            "lastName": "test",
                            "iconImageUrl": "https://example.com",
                            "content": "Hi",
                            "sentAt": "2021-04-01T19:01:00+09:00"
                        }
                    ]
                }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return empty list when get messages with invalid chatID`() {
        val chatID = "00000000-0000-0000-0000-000000000000"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/chats/$chatID/messages")
                    .header("X-UID", "test"),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": []
                }
                """,
                true,
            ),
        )
    }
}
