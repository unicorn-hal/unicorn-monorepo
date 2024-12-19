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
class MessageDeleteByUserIDTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 204 when delete message`() {
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/$userID/messages")
                    .header("X-UID", userID),
            )

        result.andExpect(status().isNoContent)
    }

    @Test
    fun `should return 400 when delete message with invalid userID`() {
        val userID = "not_found"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/$userID/messages")
                    .header("X-UID", userID),
            )

        result.andExpect(status().isBadRequest)
    }
}
