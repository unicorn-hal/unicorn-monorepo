package com.unicorn.api.controller.hospital_news

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
@Sql("/db/call_support/Insert_Call_Support_Data.sql")
@Sql("/db/hospital_news/Insert_HospitalNews_data.sql")
class HospitalNewsDeleteTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 204 when hospital news is deleted`() {
        val doctorID = "doctor"
        val hospitalID = "d8bfa31d-54b9-4c64-a499-6c522517e5f7"
        val hospitalNewsID = "d1b2f9d0-9d9b-4625-bc38-54b75d3a0127"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/hospitals/$hospitalID/news/$hospitalNewsID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", doctorID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isNoContent)
    }

    @Test
    fun `should return 400 when doctor not found`() {
        val doctorID = "not_found_doctor"
        val hospitalID = "d8bfa31d-54b9-4c64-a499-6c522517e5f7"
        val hospitalNewsID = "d1b2f9d0-9d9b-4625-bc38-54b75d3a0127"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/hospitals/$hospitalID/news/$hospitalNewsID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", doctorID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when hospital not found`() {
        val doctorID = "not_found_doctor"
        val hospitalID = "d8bfa31d-54b9-4c64-a499-6c522517e5e1"
        val hospitalNewsID = "d1b2f9d0-9d9b-4625-bc38-54b75d3a0127"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/hospitals/$hospitalID/news/$hospitalNewsID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", doctorID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when hospital news not found`() {
        val doctorID = "not_found_doctor"
        val hospitalID = "d8bfa31d-54b9-4c64-a499-6c522517e5f7"
        val hospitalNewsID = "d1b2f9d0-9d9b-4625-bc38-54b75d3a0000"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/hospitals/$hospitalID/news/$hospitalNewsID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", doctorID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isBadRequest)
    }
}
