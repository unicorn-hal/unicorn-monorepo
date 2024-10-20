package com.unicorn.api.controller.hospital

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

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/db/hospital/Insert_Hospital_Data.sql")
class HospitalsGetTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 with correct hospital data`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/hospitals").headers(HttpHeaders().apply {
            add("X-UID", "test-uid")
        }))

        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            {
                "data": [
                    {
                        "hospitalID": "d8bfa31d-54b9-4c64-a499-6c522517e5f7",
                        "hospitalName": "きくち内科医院",
                        "address": "静岡県静岡市駿河区新川2-8-3",
                        "postalCode": "4228064",
                        "phoneNumber": "0542847171"
                    },
                    {
                        "hospitalID": "762a7a7e-41e4-46c2-b36c-f2b302cae3e7",
                        "hospitalName": "内科杉山医院",
                        "address": "静岡県静岡市葵区水道町10-5",
                        "postalCode": "4200008",
                        "phoneNumber": "0542712377"
                    }
                ]
            }
        """.trimIndent(), true))
    }

}
