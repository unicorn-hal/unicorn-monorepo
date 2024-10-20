package com.unicorn.api.controller.hospital

import com.unicorn.api.application.hospital.HospitalDto
import com.unicorn.api.application.hospital.HospitalQueryService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import java.util.*

@TestPropertySource(locations=["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/db/hospital/Insert_Hospital_Data.sql")
class HospitalGetByIdTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when hospital is found by ID`() {
        val hospitalIDString = "d8bfa31d-54b9-4c64-a499-6c522517e5f7"
        val hospitalID = UUID.fromString(hospitalIDString)
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/hospitals/$hospitalID").headers(HttpHeaders().apply {
            add("X-UID", "test-uid")
        }))

        result.andExpect(status().isOk)
        result.andExpect(content().json("""{
            "hospitalID": "$hospitalID",
            "hospitalName": "きくち内科医院",
            "address": "静岡県静岡市駿河区新川2-8-3",
            "postalCode": "4228064",
            "phoneNumber": "0542847171"
        }""".trimIndent(), true))
    }

    @Test
    fun `should return 404 when hospital is not found by ID`() {
        val hospitalID = UUID.randomUUID()

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/hospitals/$hospitalID").headers(HttpHeaders().apply {
            add("X-UID", "test-uid")
        }))

        result.andExpect(status().isNotFound)
    }

    @Test
    fun `should return 400 when an invalid UUID is provided`() {
        val invalidHospitalID = "Invalid-UUID"

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/hospitals/$invalidHospitalID").headers(HttpHeaders().apply {
            add("X-UID", "test-uid")
        }))

        result.andExpect(status().isBadRequest)
    }
}
