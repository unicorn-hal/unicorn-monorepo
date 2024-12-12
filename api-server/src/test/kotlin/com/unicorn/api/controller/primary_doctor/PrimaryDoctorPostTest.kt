package com.unicorn.api.controller.primary_doctor

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.util.*
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
@Sql("/db/primary_doctor/Insert_PrimaryDoctor_Data.sql")
class PrimaryDoctorPostTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when create primary doctor`() {
        val primaryDoctorRequest =
            PrimaryDoctorRequest(
                doctorID = "doctor3",
            )
        val userID = "testtest"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/primary_doctors")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(primaryDoctorRequest)),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "userID": "$userID",
                    "doctorID": "${primaryDoctorRequest.doctorID}"
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 400 when create primary doctor for not found doctor`() {
        val primaryDoctorRequest =
            PrimaryDoctorRequest(
                doctorID = "notfound",
            )
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/primary_doctors")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(primaryDoctorRequest)),
            )
        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "errorType": "Doctor not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when create primary doctor for not found user`() {
        val primaryDoctorRequest =
            PrimaryDoctorRequest(
                doctorID = "doctor",
            )
        val userID = "notfound"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/primary_doctors")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(primaryDoctorRequest)),
            )
        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "errorType": "User not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when already exists primary doctor`() {
        val primaryDoctorRequest =
            PrimaryDoctorRequest(
                doctorID = "doctor",
            )
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/primary_doctors")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(primaryDoctorRequest)),
            )
        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "errorType": "Primary doctor already exists"
                }
                """.trimIndent(),
                true,
            ),
        )
    }
}
