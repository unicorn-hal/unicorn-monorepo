package com.unicorn.api.controller.doctor

import com.fasterxml.jackson.databind.ObjectMapper
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
@Sql("/db/doctor/Insert_Deleted_Doctor_Data.sql")
class DoctorPostTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when doctor is created`() {
        val doctor =
            DoctorPostRequest(
                hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
                firstName = "test",
                lastName = "test",
                email = "test@test.com",
                phoneNumber = "1234567890",
                doctorIconUrl = "https://example.com",
                departments = listOf(UUID.fromString("b68a87a3-b7f1-4b85-b0ab-6c620d68d791")),
                chatSupportStartHour = LocalTime.of(9, 0, 0),
                chatSupportEndHour = LocalTime.of(17, 0, 0),
                callSupportStartHour = LocalTime.of(9, 0, 0),
                callSupportEndHour = LocalTime.of(17, 0, 0),
            )
        val uid = "doctor3"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/doctors").headers(
                    HttpHeaders().apply {
                        add("X-UID", uid)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(doctor)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "hospitalID": "${doctor.hospitalID}",
                    "email": "${doctor.email}",
                    "phoneNumber": "${doctor.phoneNumber}",
                    "firstName": "${doctor.firstName}",
                    "lastName": "${doctor.lastName}",
                    "doctorIconUrl": "${doctor.doctorIconUrl}",
                    "departments": ${doctor.departments},
                    "chatSupportStartHour": "${doctor.chatSupportStartHour}",
                    "chatSupportEndHour": "${doctor.chatSupportEndHour}",
                    "callSupportStartHour": "${doctor.callSupportStartHour}",
                    "callSupportEndHour": "${doctor.callSupportEndHour}"
                }
                """.trimIndent(),
            ),
        )
    }

    // 論理削除されていた医師を復元するテスト
    @Test
    fun `should return 200 when doctor is restored`() {
        val doctorPostRequest =
            DoctorPostRequest(
                hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
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

        val uid = "doctor2"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/doctors").headers(
                    HttpHeaders().apply {
                        add("X-UID", uid)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(doctorPostRequest)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "hospitalID": "${doctorPostRequest.hospitalID}",
                    "email": "${doctorPostRequest.email}",
                    "phoneNumber": "${doctorPostRequest.phoneNumber}",
                    "firstName": "${doctorPostRequest.firstName}",
                    "lastName": "${doctorPostRequest.lastName}",
                    "doctorIconUrl": "${doctorPostRequest.doctorIconUrl}",
                    "departments": ${doctorPostRequest.departments},
                    "chatSupportStartHour": "${doctorPostRequest.chatSupportStartHour}",
                    "chatSupportEndHour": "${doctorPostRequest.chatSupportEndHour}",
                    "callSupportStartHour": "${doctorPostRequest.callSupportStartHour}",
                    "callSupportEndHour": "${doctorPostRequest.callSupportEndHour}"
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 400 when account not found`() {
        val doctorPostRequest =
            DoctorPostRequest(
                hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
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

        val uid = "not-found"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/doctors").headers(
                    HttpHeaders().apply {
                        add("X-UID", uid)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(doctorPostRequest)),
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
    fun `should return 400 when account is not doctor`() {
        val doctorPostRequest =
            DoctorPostRequest(
                hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
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

        val uid = "doctor4"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/doctors").headers(
                    HttpHeaders().apply {
                        add("X-UID", uid)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(doctorPostRequest)),
            )

        result.andExpect(status().isBadRequest)

        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Account is not doctor"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when doctor already exists`() {
        val doctorPostRequest =
            DoctorPostRequest(
                hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
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

        val uid = "doctor"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/doctors").headers(
                    HttpHeaders().apply {
                        add("X-UID", uid)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(doctorPostRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Doctor already exists"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when hospital not found`() {
        val doctorPostRequest =
            DoctorPostRequest(
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

        val uid = "doctor3"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/doctors").headers(
                    HttpHeaders().apply {
                        add("X-UID", uid)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(doctorPostRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Hospital not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    // 存在しないdepartmentIDを指定した場合のテスト
    @Test
    fun `should return 400 when department not found`() {
        val doctorPostRequest =
            DoctorPostRequest(
                hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
                firstName = "test",
                lastName = "test",
                email = "test@test.com",
                phoneNumber = "1234567890",
                doctorIconUrl = "https://example.com",
                departments =
                    listOf(
                        UUID.fromString("653c078c-50c5-4e88-ae65-cff65e1e6c1c"),
                        UUID.fromString("dcda6a0b-fb0f-4142-a8b5-b19264da90fc"),
                    ),
                chatSupportStartHour = LocalTime.of(9, 0, 0),
                chatSupportEndHour = LocalTime.of(17, 0, 0),
                callSupportStartHour = LocalTime.of(9, 0, 0),
                callSupportEndHour = LocalTime.of(17, 0, 0),
            )

        val uid = "doctor3"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/doctors").headers(
                    HttpHeaders().apply {
                        add("X-UID", uid)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(doctorPostRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Department not found: ${doctorPostRequest.departments[0]}, ${doctorPostRequest.departments[1]}"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when duplicate departmentID`() {
        val doctorPostRequest =
            DoctorPostRequest(
                hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
                firstName = "test",
                lastName = "test",
                email = "test@test.com",
                phoneNumber = "1234567890",
                doctorIconUrl = "https://example.com",
                departments =
                    listOf(
                        UUID.fromString("b68a87a3-b7f1-4b85-b0ab-6c620d68d791"),
                        UUID.fromString("b68a87a3-b7f1-4b85-b0ab-6c620d68d791"),
                    ),
                chatSupportStartHour = LocalTime.of(9, 0, 0),
                chatSupportEndHour = LocalTime.of(17, 0, 0),
                callSupportStartHour = LocalTime.of(9, 0, 0),
                callSupportEndHour = LocalTime.of(17, 0, 0),
            )

        val uid = "doctor3"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/doctors").headers(
                    HttpHeaders().apply {
                        add("X-UID", uid)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(doctorPostRequest)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "Duplicate departmentID: ${doctorPostRequest.departments[0]}"
                }
                """.trimIndent(),
                true,
            ),
        )
    }
}
