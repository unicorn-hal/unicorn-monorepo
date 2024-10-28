package com.unicorn.api.controller.doctor

import com.fasterxml.jackson.databind.ObjectMapper
import com.unicorn.api.domain.doctor.DoctorID
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
import java.time.LocalTime
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/doctor/Insert_Parent_Account_Data.sql")
@Sql("/db/hospital/Insert_Hospital_Data.sql")
@Sql("/db/department/Insert_Department_Data.sql")
@Sql("/db/doctor/Insert_Doctor_Data.sql")
@Sql("/db/doctor_department/Insert_Doctor_Department_Data.sql")
@Sql("/db/chat_support/Insert_Chat_Support_Data.sql")
@Sql("/db/call_support/Insert_Call_Support_Data.sql")
class DoctorPutTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when doctor is updated`() {
        val doctorPutRequest = DoctorPutRequest(
            hospitalID = UUID.fromString("762a7a7e-41e4-46c2-b36c-f2b302cae3e7"),
            firstName = "sample",
            lastName = "sample",
            email = "test@test.com",
            phoneNumber = "1234567890",
            doctorIconUrl = "https://example.com",
            departments = listOf(UUID.fromString("ed8fb319-798f-4b47-bcf3-2a8f6486e38d")),
            chatSupportStartHour = LocalTime.of(11, 0, 0),
            chatSupportEndHour = LocalTime.of(19, 0, 0),
            callSupportStartHour = LocalTime.of(10, 0, 0),
            callSupportEndHour = LocalTime.of(13, 0, 0),
        )

        val doctorID = DoctorID("doctor")

        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/doctors/${doctorID.value}").headers(HttpHeaders().apply {
                add("X-UID", doctorID.value)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorPutRequest))
        )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "hospitalID": "${doctorPutRequest.hospitalID}",
                    "email": "${doctorPutRequest.email}",
                    "phoneNumber": "${doctorPutRequest.phoneNumber}",
                    "firstName": "${doctorPutRequest.firstName}",
                    "lastName": "${doctorPutRequest.lastName}",
                    "doctorIconUrl": "${doctorPutRequest.doctorIconUrl}",
                    "departments": ${doctorPutRequest.departments},
                    "chatSupportStartHour": "${doctorPutRequest.chatSupportStartHour}",
                    "chatSupportEndHour": "${doctorPutRequest.chatSupportEndHour}",
                    "callSupportStartHour": "${doctorPutRequest.callSupportStartHour}",
                    "callSupportEndHour": "${doctorPutRequest.callSupportEndHour}"
                }
                """.trimIndent()
            )
        )
    }

    @Test
    fun `should return 400 when doctor is not found`() {
        val doctorPutRequest = DoctorPutRequest(
            hospitalID = UUID.fromString("762a7a7e-41e4-46c2-b36c-f2b302cae3e7"),
            firstName = "sample",
            lastName = "sample",
            email = "test@test.com",
            phoneNumber = "1234567890",
            doctorIconUrl = "https://example.com",
            departments = listOf(UUID.fromString("ed8fb319-798f-4b47-bcf3-2a8f6486e38d")),
            chatSupportStartHour = LocalTime.of(11, 0, 0),
            chatSupportEndHour = LocalTime.of(19, 0, 0),
            callSupportStartHour = LocalTime.of(10, 0, 0),
            callSupportEndHour = LocalTime.of(13, 0, 0),
        )

        val doctorID = DoctorID("not-found")

        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/doctors/${doctorID.value}").headers(HttpHeaders().apply {
                add("X-UID", doctorID.value)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorPutRequest))
        )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Doctor not found"
                }
                """.trimIndent(), true
            )
        )
    }

    @Test
    fun `should return 400 when hospital is not found`() {
        val doctorPutRequest = DoctorPutRequest(
            hospitalID = UUID.fromString("653c078c-50c5-4e88-ae65-cff65e1e6c1c"),
            firstName = "test",
            lastName = "test",
            email = "test@test.com",
            phoneNumber = "1234567890",
            doctorIconUrl = "https://example.com",
            departments = listOf(UUID.fromString("cd273b1b-0c3b-4b89-b2b9-01b21832b44c")),
            chatSupportStartHour = LocalTime.of(9, 0, 0),
            chatSupportEndHour = LocalTime.of(17, 0, 0),
            callSupportStartHour = LocalTime.of(9, 0, 0),
            callSupportEndHour = LocalTime.of(17, 0, 0),
        )

        val doctorID = DoctorID("doctor")

        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/doctors/${doctorID.value}").headers(HttpHeaders().apply {
                add("X-UID", doctorID.value)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorPutRequest))
        )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Hospital not found"
                }
                """.trimIndent(), true
            )
        )

    }

    @Test
    fun `should return 400 when department is not found`() {
        val doctorPutRequest = DoctorPutRequest(
            hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
            firstName = "test",
            lastName = "test",
            email = "test@test.com",
            phoneNumber = "1234567890",
            doctorIconUrl = "https://example.com",
            departments = listOf(
                UUID.fromString("653c078c-50c5-4e88-ae65-cff65e1e6c1c"),
                UUID.fromString("dcda6a0b-fb0f-4142-a8b5-b19264da90fc")
            ),
            chatSupportStartHour = LocalTime.of(9, 0, 0),
            chatSupportEndHour = LocalTime.of(17, 0, 0),
            callSupportStartHour = LocalTime.of(9, 0, 0),
            callSupportEndHour = LocalTime.of(17, 0, 0),
        )

        val doctorID = DoctorID("doctor")

        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/doctors/${doctorID.value}").headers(HttpHeaders().apply {
                add("X-UID", doctorID.value)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorPutRequest))
        )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Department not found: ${doctorPutRequest.departments[0]}, ${doctorPutRequest.departments[1]}"
                }
                """.trimIndent(), true
            )
        )
    }


}