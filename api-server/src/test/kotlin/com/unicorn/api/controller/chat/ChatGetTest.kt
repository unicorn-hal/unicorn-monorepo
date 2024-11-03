package com.unicorn.api.controller.chat

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
class ChatGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when get chat`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .get("/chats")
                    .header("X-UID", "test"),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                    {
                        "data": [
                            {
                                "chatID": "e38fd3d0-99bc-11ef-8e52-cfa170f7b603",
                                "doctor": {
                                    "doctorID": "doctor",
                                    "doctorIconUrl": "https://example.com",
                                    "firstName": "test",
                                    "lastName": "test"
                                },
                                "user": {
                                    "userID": "test",
                                    "userIconUrl": "https://example.com",
                                    "firstName": "test",
                                    "lastName": "test"
                                },
                                "latestMessageText": "Hi",
                                "latestMessageSentAt": "2021-04-01T19:01:00+09:00"
                            }
                        ]
                    }
                """,
            ),
        )
    }

    @Test
    fun `should return 200 when get uid not found`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .get("/chats")
                    .header("X-UID", "not_found"),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                    {
                        "data": []
                    }
                """,
            ),
        )
    }
}
