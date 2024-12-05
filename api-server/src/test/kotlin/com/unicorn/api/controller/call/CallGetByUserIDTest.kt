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
                            "userID": "$userID",
                            "doctor": {
                                "doctorID": "doctor",
                                "hospital": {
                                    "hospitalID": "d8bfa31d-54b9-4c64-a499-6c522517e5f7",
                                    "hospitalName": "きくち内科医院"
                                },
                                "email": "test@test.com",
                                "phoneNumber": "1234567890",
                                "firstName": "test",
                                "lastName": "test",
                                "doctorIconUrl": "https://example.com",
                                "departments": [
                                    {
                                        "departmentID": "b68a87a3-b7f1-4b85-b0ab-6c620d68d791",
                                        "departmentName": "循環器内科"
                                    },
                                    {
                                        "departmentID": "a1dcb69e-472f-4a57-90a2-f2c63b62ec90",
                                        "departmentName": "総合内科"
                                    }
                                ],
                                "chatSupportHours": "09:00-18:00",
                                "callSupportHours": "09:00-18:00"
                            }
                        },
                        {
                            "callReservationID": "221177ed-92f8-a956-825f-c31b2cad8b10",
                            "userID": "$userID",
                            "doctor": {
                                "doctorID": "doctor2",
                                "hospital": {
                                    "hospitalID": "762a7a7e-41e4-46c2-b36c-f2b302cae3e7",
                                    "hospitalName": "内科杉山医院"
                                },
                                "email": "test@test.com",
                                "phoneNumber": "1234567899",
                                "firstName": "山田",
                                "lastName": "次郎",
                                "doctorIconUrl": "https://example.com",
                                "departments": [
                                    {
                                        "departmentID": "cd273b1b-0c3b-4b89-b2b9-01b21832b44c",
                                        "departmentName": "呼吸器内科"
                                    }
                                ],
                                "chatSupportHours": "09:00-15:00",
                                "callSupportHours": "09:00-15:00"
                            }
                        },
                        {
                            "callReservationID": "221177ed-92f8-a956-825f-c31b2cad8b22",
                            "userID": "$userID",
                            "doctor": {
                                "doctorID": "doctor2",
                                "hospital": {
                                    "hospitalID": "762a7a7e-41e4-46c2-b36c-f2b302cae3e7",
                                    "hospitalName": "内科杉山医院"
                                },
                                "email": "test@test.com",
                                "phoneNumber": "1234567899",
                                "firstName": "山田",
                                "lastName": "次郎",
                                "doctorIconUrl": "https://example.com",
                                "departments": [
                                    {
                                        "departmentID": "cd273b1b-0c3b-4b89-b2b9-01b21832b44c",
                                        "departmentName": "呼吸器内科"
                                    }
                                ],
                                "chatSupportHours": "09:00-15:00",
                                "callSupportHours": "09:00-15:00"
                            }
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
                            "userID": "$userID",
                            "doctor": {
                                "doctorID": "doctor2",
                                "hospital": {
                                    "hospitalID": "762a7a7e-41e4-46c2-b36c-f2b302cae3e7",
                                    "hospitalName": "内科杉山医院"
                                },
                                "email": "test@test.com",
                                "phoneNumber": "1234567899",
                                "firstName": "山田",
                                "lastName": "次郎",
                                "doctorIconUrl": "https://example.com",
                                "departments": [
                                    {
                                        "departmentID": "cd273b1b-0c3b-4b89-b2b9-01b21832b44c",
                                        "departmentName": "呼吸器内科"
                                    }
                                ],
                                "chatSupportHours": "09:00-15:00",
                                "callSupportHours": "09:00-15:00"
                            }
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
