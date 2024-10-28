package com.unicorn.api.controller.doctor

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
@Sql("/db/doctor/Insert_Temporary_Doctor_Data.sql")
@Sql("/db/doctor_department/Insert_Doctor_Department_Data.sql")
@Sql("/db/chat_support/Insert_Chat_Support_Data.sql")
@Sql("/db/call_support/Insert_Call_Support_Data.sql")
class DoctorGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

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

    val doctorDto2 = DoctorDto(
        doctorID = "doctor2",
        hospital = HospitalDto(
            hospitalID = "762a7a7e-41e4-46c2-b36c-f2b302cae3e7",
            hospitalName = "内科杉山医院"
        ),
        firstName = "田中",
        lastName = "太郎",
        doctorIconUrl = "https://example.com",
        departments = listOf(
            DepartmentDto(
                departmentID = "b68a87a3-b7f1-4b85-b0ab-6c620d68d791",
                departmentName = "循環器内科"
            )
        ),
        email = "test@test.com",
        phoneNumber = "1234567890",
        chatSupportHours = "09:00-18:00",
        callSupportHours = "09:00-18:00",
    )

    @Nested
    inner class GetByDoctorID {
        @Test
        fun `should return 200 when doctor is found`() {

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
            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/hospitals/${doctorDto.hospital.hospitalID}/doctors")
                    .headers(HttpHeaders().apply {
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

    @Nested
    inner class GetDoctors {
        @Test
        fun `should return 200 when all doctors are found`() {
            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors").headers(HttpHeaders().apply {
                    add("X-UID", "doctor")
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
                                },
                                {
                                    "doctorID": "${doctorDto2.doctorID}",
                                    "hospital": {
                                        "hospitalID": "${doctorDto2.hospital.hospitalID}",
                                        "hospitalName": "${doctorDto2.hospital.hospitalName}"
                                    },
                                    "email": "${doctorDto2.email}",
                                    "phoneNumber": "${doctorDto2.phoneNumber}",
                                    "firstName": "${doctorDto2.firstName}",
                                    "lastName": "${doctorDto2.lastName}",
                                    "doctorIconUrl": "${doctorDto2.doctorIconUrl}",
                                    "departments": [
                                        {
                                            "departmentID": "${doctorDto2.departments[0].departmentID}",
                                            "departmentName": "${doctorDto2.departments[0].departmentName}"
                                        }
                                    ],
                                    "chatSupportHours": "${doctorDto2.chatSupportHours}",
                                    "callSupportHours": "${doctorDto2.callSupportHours}"
                                }
                            ]
                        }
                    """.trimIndent()
                )
            )
        }

        @Test
        fun `should return 200 when doctors are found by departmentID`() {
            val departmentID = "b68a87a3-b7f1-4b85-b0ab-6c620d68d791"

            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors").headers(HttpHeaders().apply {
                    add("X-UID", "doctor")
                })
                    .param("departmentID", departmentID)
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
                                },
                                {
                                    "doctorID": "${doctorDto2.doctorID}",
                                    "hospital": {
                                        "hospitalID": "${doctorDto2.hospital.hospitalID}",
                                        "hospitalName": "${doctorDto2.hospital.hospitalName}"
                                    },
                                    "email": "${doctorDto2.email}",
                                    "phoneNumber": "${doctorDto2.phoneNumber}",
                                    "firstName": "${doctorDto2.firstName}",
                                    "lastName": "${doctorDto2.lastName}",
                                    "doctorIconUrl": "${doctorDto2.doctorIconUrl}",
                                    "departments": [
                                        {
                                            "departmentID": "${doctorDto2.departments[0].departmentID}",
                                            "departmentName": "${doctorDto2.departments[0].departmentName}"
                                        }
                                    ],
                                    "chatSupportHours": "${doctorDto2.chatSupportHours}",
                                    "callSupportHours": "${doctorDto2.callSupportHours}"
                                }
                            ]
                        }
                    """.trimIndent()
                )
            )
        }

        @Test
        fun `should return 200 when doctors are found by doctorName`() {
            val doctorName = "es"

            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors").headers(HttpHeaders().apply {
                    add("X-UID", "doctor")
                })
                    .param("doctorName", doctorName)
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

        // 病院名で検索するテストを追加
        @Test
        fun `should return 200 when doctors are found by hospitalName`() {
            val hospitalName = "くち内科"

            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors").headers(HttpHeaders().apply {
                    add("X-UID", "doctor")
                })
                    .param("hospitalName", hospitalName)
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
        fun `should return 200 when doctors are found by doctorName and hospitalName`() {
            val doctorName = "es"
            val hospitalName = "くち内科"

            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors").headers(HttpHeaders().apply {
                    add("X-UID", "doctor")
                })
                    .param("doctorName", doctorName)
                    .param("hospitalName", hospitalName)
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
        fun `should return 200 when doctors are found by departmentID and hospitalName`() {
            val departmentID = "b68a87a3-b7f1-4b85-b0ab-6c620d68d791"
            val hospitalName = "くち内科"

            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors").headers(HttpHeaders().apply {
                    add("X-UID", "doctor")
                })
                    .param("departmentID", departmentID)
                    .param("hospitalName", hospitalName)
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
        fun `should return 200 when doctors are found by departmentID and doctorName`() {
            val departmentID = "b68a87a3-b7f1-4b85-b0ab-6c620d68d791"
            val doctorName = "es"

            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors").headers(HttpHeaders().apply {
                    add("X-UID", "doctor")
                })
                    .param("departmentID", departmentID)
                    .param("doctorName", doctorName)
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
        fun `should return 200 when doctors are found by doctorName, hospitalName and departmentID`() {
            val doctorName = "es"
            val hospitalName = "くち内科"
            val departmentID = "b68a87a3-b7f1-4b85-b0ab-6c620d68d791"

            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors").headers(HttpHeaders().apply {
                    add("X-UID", "doctor")
                })
                    .param("doctorName", doctorName)
                    .param("hospitalName", hospitalName)
                    .param("departmentID", departmentID)
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
        fun `should return 200 when doctors are not found by doctorName, hospitalName and departmentID`() {
            val doctorName = "太郎"
            val hospitalName = "くち内科"
            val departmentID = "b68a87a3-b7f1-4b85-b0ab-6c620d68d791"

            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors").headers(HttpHeaders().apply {
                    add("X-UID", "doctor")
                })
                    .param("doctorName", doctorName)
                    .param("hospitalName", hospitalName)
                    .param("departmentID", departmentID)
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