package com.unicorn.api.controller.primary_doctor

import com.unicorn.api.query_service.doctor.DepartmentDto
import com.unicorn.api.query_service.doctor.DoctorDto
import com.unicorn.api.query_service.doctor.HospitalDto
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
@Sql("/db/primary_doctor/Insert_PrimaryDoctor_Data.sql")
@Sql("/db/primary_doctor/Insert_Call_Support_Data.sql")
@Sql("/db/primary_doctor/Insert_Chat_Support_Data.sql")
class PrimaryDoctorGetTest {

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
        firstName = "山田",
        lastName = "次郎",
        doctorIconUrl = "https://example.com",
        departments = listOf(
            DepartmentDto(
                departmentID = "cd273b1b-0c3b-4b89-b2b9-01b21832b44c",
                departmentName = "呼吸器内科"
            )
        ),
        email = "test@test.com",
        phoneNumber = "1234567899",
        chatSupportHours = "09:00-15:00",
        callSupportHours = "09:00-15:00",
    )

    @Test
    fun `should return 200 when doctor is found`() {
        val uid = "12345"

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/primary_doctors").headers(HttpHeaders().apply {
                add("X-UID", uid)
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
    fun `should return 200 when all doctors are found`() {
        val uid = "test"

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/primary_doctors").headers(HttpHeaders().apply {
                add("X-UID", uid)
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
    fun `should return 400 when user is not found`() {
        val uid = "not_exist_user_id"

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/primary_doctors").headers(HttpHeaders().apply {
                add("X-UID", uid)
            })
                .contentType(MediaType.APPLICATION_JSON)
        )

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 200 with empty list when userID exists but primaryDoctor is not registered`() {
        val uid = "testtest" // 存在する user だが primaryDoctor をまだ登録していない

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/primary_doctors").headers(HttpHeaders().apply {
                add("X-UID", uid)
            })
                .contentType(MediaType.APPLICATION_JSON)
        )

        result.andExpect(status().isOk)
            .andExpect(content().json("""{"data":[]}""")) // 空のリストが返されることを確認
    }
}