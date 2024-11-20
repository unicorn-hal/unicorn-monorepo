package com.unicorn.api.controller.call

import com.fasterxml.jackson.databind.ObjectMapper
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
import java.time.OffsetDateTime
import kotlin.test.Test

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
@Sql("/db/call_support/Insert_Call_Support_Data.sql")
@Sql("/db/call/Insert_New_Call_Data.sql")
class CallPutTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when call reservation is updated`() {
        val userID = "test"
        val callReservationID = "211177ed-92f8-a956-825f-c31b2cad8b15"

        val call =
            CallPostRequest(
                userID = userID,
                doctorID = "doctor",
                callStartTime = OffsetDateTime.parse("2024-10-12T15:00:00+09:00"),
                callEndTime = OffsetDateTime.parse("2024-10-12T15:30:00+09:00"),
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/calls/$callReservationID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(call)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "callReservationID": "$callReservationID",
                    "doctorID": "${call.doctorID}",
                    "userID": "$userID",
                    "callStartTime": "2024-10-12T15:00:00+09:00",
                    "callEndTime": "2024-10-12T15:30:00+09:00"
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 400 when user not found`() {
        val userID = "not_found_user"
        val callReservationID = "211177ed-92f8-a956-825f-c31b2cad8b15"

        val call =
            CallPostRequest(
                userID = userID,
                doctorID = "doctor",
                callStartTime = OffsetDateTime.parse("2024-10-12T15:00:00+09:00"),
                callEndTime = OffsetDateTime.parse("2024-10-12T15:30:00+09:00"),
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/calls/$callReservationID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(call)),
            )

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when doctor not found`() {
        val userID = "test"
        val callReservationID = "211177ed-92f8-a956-825f-c31b2cad8b15"

        val call =
            CallPostRequest(
                userID = userID,
                doctorID = "not_found_doctor",
                callStartTime = OffsetDateTime.parse("2024-10-12T15:00:00+09:00"),
                callEndTime = OffsetDateTime.parse("2024-10-12T15:30:00+09:00"),
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/calls/$callReservationID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(call)),
            )

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when call reservation not found`() {
        val userID = "test"
        val callReservationID = "211177ed-92f8-a956-825f-c31b2cad8b16"

        val call =
            CallPostRequest(
                userID = userID,
                doctorID = "not_found_doctor",
                callStartTime = OffsetDateTime.parse("2024-10-12T15:00:00+09:00"),
                callEndTime = OffsetDateTime.parse("2024-10-12T15:30:00+09:00"),
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/calls/$callReservationID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(call)),
            )

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when call start time do not match the doctor's support hours`() {
        val userID = "test"
        val callReservationID = "211177ed-92f8-a956-825f-c31b2cad8b15"

        val call =
            CallPostRequest(
                userID = userID,
                doctorID = "doctor",
                callStartTime = OffsetDateTime.parse("2024-10-12T08:00:00+09:00"),
                callEndTime = OffsetDateTime.parse("2024-10-12T15:30:00+09:00"),
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/calls/$callReservationID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(call)),
            )

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when call end time do not match the doctor's support hours`() {
        val userID = "test"
        val callReservationID = "211177ed-92f8-a956-825f-c31b2cad8b15"

        val call =
            CallPostRequest(
                userID = userID,
                doctorID = "doctor",
                callStartTime = OffsetDateTime.parse("2024-10-12T17:00:00+09:00"),
                callEndTime = OffsetDateTime.parse("2024-10-12T18:30:00+09:00"),
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/calls/$callReservationID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(call)),
            )

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when a call reservation overlaps with an existing reservation`() {
        val userID = "test"
        val doctorID = "doctor"
        val callReservationID = "211177ed-92f8-a956-825f-c31b2cad8b15"
        val conflictingCallStartTime = OffsetDateTime.parse("2021-01-01T10:00:00+09:00")
        val conflictingCallEndTime = OffsetDateTime.parse("2021-01-01T11:00:00+09:00")

        val conflictingCall =
            CallPostRequest(
                userID = userID,
                doctorID = doctorID,
                callStartTime = conflictingCallStartTime,
                callEndTime = conflictingCallEndTime,
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/calls/$callReservationID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(conflictingCall)),
            )

        // 予約時間が重複しているため、400 Bad Requestが返されることを確認
        result.andExpect(status().isBadRequest)
    }
}
