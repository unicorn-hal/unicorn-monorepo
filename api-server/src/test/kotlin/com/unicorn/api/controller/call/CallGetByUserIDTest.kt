package com.unicorn.api.controller.call

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
class CallGetByUserIDTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when call reservation is called`() {
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users/$userID/calls").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": [
                        {
                            "callReservationID": "551177ed-92f8-a956-825f-c31b2cad8b10",
                            "doctorID": "doctor",
                            "userID": "$userID"
                        },
                        {
                            "callReservationID": "221177ed-92f8-a956-825f-c31b2cad8b10",
                            "doctorID": "doctor2",
                            "userID": "$userID"
                        },
                        {
                            "callReservationID": "221177ed-92f8-a956-825f-c31b2cad8b22",
                            "doctorID": "doctor2",
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
    fun `should not return call reservations beyond one year`() {
        val userID = "12345"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users/$userID/calls").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": [
                        {
                            "callReservationID": "311177ed-92f8-a956-825f-c31b2cad8b15",
                            "doctorID": "doctor2",
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
    fun `should return 200 when call reservation not found`() {
        val userID = "testtest"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users/$userID/calls").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": []
                }
                """.trimIndent(),
                false,
            ),
        )
    }

    @Test
    fun `should return 400 when user not found`() {
        val userID = "not_found_user"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users/$userID/calls").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isBadRequest)
    }
}
