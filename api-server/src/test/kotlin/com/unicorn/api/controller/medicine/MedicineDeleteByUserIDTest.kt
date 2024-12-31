package com.unicorn.api.controller.medicine

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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/medicine/Insert_Medicine_Data.sql")
@Sql("/db/medicine_reminder/Insert_Medicine_Reminder_Data.sql")
class MedicineDeleteByUserIDTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 204 when medicine is deleted by userID`() {
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/$userID/medicines").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )

        result.andExpect(status().isNoContent)
    }

    @Test
    fun `should return 400 when user ID is invalid`() {
        val userID = "invalid-id"

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/users/$userID/medicines").headers(
                HttpHeaders().apply {
                    add("X-UID", userID)
                },
            )
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isBadRequest)
    }
}
