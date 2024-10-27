package com.unicorn.api.controller.doctor

import com.fasterxml.jackson.databind.ObjectMapper
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.query_service.doctor.DepartmentDto
import com.unicorn.api.query_service.doctor.DoctorDto
import com.unicorn.api.query_service.doctor.HospitalDto
import org.junit.jupiter.api.Nested
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
class DoctorGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Nested
    inner class GetByDoctorID {
        @Test
        fun `should return 200 when doctor is found`() {
            val doctorDto = DoctorDto(
                doctorID = "doctor",
                hospital = HospitalDto(
                    hospitalID = "d8bfa31d-54b9-4c64-a499-6c522517e5f7",
                    hospitalName = "きくち内科医院"
                ),
                firstName = "test",
                lastName = "test",
                doctorIconUrl = "https://example.com",
                departments = listOf(
                    DepartmentDto(
                        departmentID = "b68a87a3-b7f1-4b85-b0ab-6c620d68d791",
                        departmentName = "循環器内科"
                    ),
                    DepartmentDto(
                        departmentID = "a1dcb69e-472f-4a57-90a2-f2c63b62ec90",
                        departmentName = "総合内科"
                    )
                ),
                email = "test@test.com",
                phoneNumber = "1234567890",
                chatSupportHours = "09:00-18:00",
                callSupportHours = "09:00-18:00",
            )

            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors/${doctorDto.doctorID}").headers(HttpHeaders().apply {
                    add("X-UID", doctorDto.doctorID)
                })
                    .contentType(MediaType.APPLICATION_JSON)

            )

            result.andExpect(status().isOk)
            result.andExpect(
                content().json(
                    """
                        {
                            "doctorID": "${doctorDto.doctorID}",
                            "hospital": {
                                "hospitalID": "${doctorDto.hospital.hospitalID}",
                                "hospitalName": "${doctorDto.hospital.hospitalName}"
                            },
                            "email": "${doctorDto.email}",
                            "phoneNumber": "${doctorDto.phoneNumber}",
                            "firstName": "${doctorDto.firstName}",
                            "lastName": "${doctorDto.lastName}",
                            "doctorIconUrl": "${doctorDto.doctorIconUrl}",
                            "departments": [
                                {
                                    "departmentID": "${doctorDto.departments[0].departmentID}",
                                    "departmentName": "${doctorDto.departments[0].departmentName}"
                                },
                                {
                                    "departmentID": "${doctorDto.departments[1].departmentID}",
                                    "departmentName": "${doctorDto.departments[1].departmentName}"
                                }
                            ],
                            "chatSupportHours": "${doctorDto.chatSupportHours}",
                            "callSupportHours": "${doctorDto.callSupportHours}"
                        }
                    """.trimIndent()
                )
            )
        }

        @Test
        fun `should return 404 when doctor is not found`() {
            val doctorID = DoctorID("not_found_doctor")

            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors/${doctorID.value}").headers(HttpHeaders().apply {
                    add("X-UID", doctorID.value)
                })
                    .contentType(MediaType.APPLICATION_JSON)
            )

            result.andExpect(status().isNotFound)
        }
    }

    @Nested
    inner class GetByHospitalID {
        @Test
        fun `should return 200 when doctors are found`() {
            val doctorDto = DoctorDto(
                doctorID = "doctor",
                hospital = HospitalDto(
                    hospitalID = "d8bfa31d-54b9-4c64-a499-6c522517e5f7",
                    hospitalName = "きくち内科医院"
                ),
                firstName = "test",
                lastName = "test",
                doctorIconUrl = "https://example.com",
                departments = listOf(
                    DepartmentDto(
                        departmentID = "b68a87a3-b7f1-4b85-b0ab-6c620d68d791",
                        departmentName = "循環器内科"
                    ),
                    DepartmentDto(
                        departmentID = "a1dcb69e-472f-4a57-90a2-f2c63b62ec90",
                        departmentName = "総合内科"
                    )
                ),
                email = "test@test.com",
                phoneNumber = "1234567890",
                chatSupportHours = "09:00-18:00",
                callSupportHours = "09:00-18:00",
            )

            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/hospitals/${doctorDto.hospital.hospitalID}/doctors").headers(HttpHeaders().apply {
                    add("X-UID", doctorDto.doctorID)
                })
                    .contentType(MediaType.APPLICATION_JSON)
            )

            result.andExpect(status().isOk)
            result.andExpect(
                content().json(
                    """
                        {
                            "data": [
                                {
                                    "doctorID": "${doctorDto.doctorID}",
                                    "hospital": {
                                        "hospitalID": "${doctorDto.hospital.hospitalID}",
                                        "hospitalName": "${doctorDto.hospital.hospitalName}"
                                    },
                                    "email": "${doctorDto.email}",
                                    "phoneNumber": "${doctorDto.phoneNumber}",
                                    "firstName": "${doctorDto.firstName}",
                                    "lastName": "${doctorDto.lastName}",
                                    "doctorIconUrl": "${doctorDto.doctorIconUrl}",
                                    "departments": [
                                        {
                                            "departmentID": "${doctorDto.departments[0].departmentID}",
                                            "departmentName": "${doctorDto.departments[0].departmentName}"
                                        },
                                        {
                                            "departmentID": "${doctorDto.departments[1].departmentID}",
                                            "departmentName": "${doctorDto.departments[1].departmentName}"
                                        }
                                    ],
                                    "chatSupportHours": "${doctorDto.chatSupportHours}",
                                    "callSupportHours": "${doctorDto.callSupportHours}"
                                }
                           ]
                        }
                      """.trimIndent()
                )
            )
        }

        @Test
        fun `should return empty list when doctors are not found`() {
            val hospitalID = UUID.randomUUID().toString()

            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/hospitals/$hospitalID/doctors").headers(HttpHeaders().apply {
                    add("X-UID", "doctor")
                })
                    .contentType(MediaType.APPLICATION_JSON)
            )

            result.andExpect(status().isOk)
            result.andExpect(
                content().json(
                    """
                        {
                            "data": []
                        }
                    """.trimIndent()
                )
            )
        }
    }
}