package com.unicorn.api.controller.call

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/primary_doctor/Insert_Parent_Account_Data.sql")
@Sql("/db/primary_doctor/Insert_User_Data.sql")
@Sql("/db/primary_doctor/Insert_Hospital_Data.sql")
@Sql("/db/primary_doctor/Insert_Department_Data.sql")
@Sql("/db/primary_doctor/Insert_Doctor_Data.sql")
@Sql("/db/primary_doctor/Insert_Doctor_Department_Data.sql")
@Sql("/db/primary_doctor/Insert_Call_Support_Data.sql")
@Sql("/db/primary_doctor/Insert_Chat_Support_Data.sql")
@Sql("/db/call/Insert_New_Call_Data.sql")
class CallGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when call reservation is called`() {
        val userID = "test"
        val doctorID = "doctor"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/calls").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .param("doctorID", doctorID)
                    .param("userID", userID),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": [
                        {
                            "callReservationID": "551177ed-92f8-a956-825f-c31b2cad8b10",
                            "doctorID": "$doctorID",
                            "userID": "$userID"
                        }
                    ]
                }
                """.trimIndent(),
                false,
            ),
        )
    }

    @Test
    fun `multiple call reservations for a user and doctor`() {
        val userID = "test"
        val doctorID = "doctor2"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/calls").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .param("doctorID", doctorID)
                    .param("userID", userID),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": [
                        {
                            "callReservationID": "221177ed-92f8-a956-825f-c31b2cad8b10",
                            "doctorID": "$doctorID",
                            "userID": "$userID"
                        },
                        {
                            "callReservationID": "221177ed-92f8-a956-825f-c31b2cad8b22",
                            "doctorID": "$doctorID",
                            "userID": "$userID"
                        }
                    ]
                }
                """.trimIndent(),
                false,
            ),
        )
    }

    @Test
    fun `should return 400 when user not found`() {
        val userID = "not_found"
        val doctorID = "doctor"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/calls").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .param("doctorID", doctorID)
                    .param("userID", userID),
            )
        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when doctor not found`() {
        val userID = "test"
        val doctorID = "not_found"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/calls").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .param("doctorID", doctorID)
                    .param("userID", userID),
            )
        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when call reservation not found`() {
        val userID = "12345"
        val doctorID = "doctor"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/calls").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .param("doctorID", doctorID)
                    .param("userID", userID),
            )
        result.andExpect(status().isBadRequest)
    }
}
