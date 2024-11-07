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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
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
    fun `should return 200 when primary doctor is created`() {
        val userID = "12345"
        val doctorIDs = listOf("doctor6")
        val primaryDoctorIDs = PrimaryDoctorPostRequest(
            doctorIDs = doctorIDs
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/primary_doctors").headers(HttpHeaders().apply {
                add("X-UID", userID)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(primaryDoctorIDs))
        )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                    {
                        "userID": "12345",
                        "doctorIDs": [
                            "doctor6"
                        ]
                    }
                """.trimIndent()
            )
        )
    }

    @Test
    fun `should return 200 when multiple primary doctors are created`() {
        val userID = "12345"
        val doctorIDs = listOf("doctor5", "doctor6")
        val primaryDoctorIDs = PrimaryDoctorPostRequest(
            doctorIDs = doctorIDs
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/primary_doctors").headers(HttpHeaders().apply {
                add("X-UID", userID)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(primaryDoctorIDs))
        )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                    {
                        "userID": "12345",
                        "doctorIDs": [
                            "doctor5",
                            "doctor6"
                        ]
                    }
                """.trimIndent()
            )
        )
    }

    @Test
    fun `should return 400 when account is a doctor`() {
        val userID = "doctor"
        val doctorIDs = listOf("doctor6")
        val primaryDoctorPostRequest = PrimaryDoctorPostRequest(
            doctorIDs = doctorIDs
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/primary_doctors").headers(HttpHeaders().apply {
                add("X-UID", userID)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(primaryDoctorPostRequest))
        )

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when doctorID is missing`() {
        val userID = "12345"
        val doctorIDs = listOf("")
        val primaryDoctorPostRequest = PrimaryDoctorPostRequest(
            doctorIDs = doctorIDs
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/primary_doctors").headers(HttpHeaders().apply {
                add("X-UID", userID)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(primaryDoctorPostRequest))
        )

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when doctorID does not exist`() {
        val userID = "12345"
        val doctorIDs = listOf("doctor000")
        val primaryDoctorPostRequest = PrimaryDoctorPostRequest(
            doctorIDs = doctorIDs
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/primary_doctors").headers(HttpHeaders().apply {
                add("X-UID", userID)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(primaryDoctorPostRequest))
        )

        result.andExpect(status().isBadRequest)
    }
}