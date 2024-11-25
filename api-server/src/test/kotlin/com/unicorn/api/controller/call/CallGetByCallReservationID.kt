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
class CallGetByCallReservationID {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when callReservationID is called`() {
        val callReservationID = "211177ed-92f8-a956-825f-c31b2cad8b15"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/calls/$callReservationID").headers(
                    HttpHeaders().apply {
                        add("X-UID", "test")
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "callReservationID": "$callReservationID",
                    "doctorID": "doctor",
                    "userID": "test",
                    "callStartTime": "2021-01-01T09:00:00+09:00",
                    "callEndTime": "2021-01-01T10:30:00+09:00"
                }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 404 when callReservationID is not found`() {
        val callReservationID = "211177ed-92f8-a956-825f-c31b2cad8b99"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/calls/$callReservationID").headers(
                    HttpHeaders().apply {
                        add("X-UID", "test")
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isNotFound)
    }
}
