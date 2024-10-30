package com.unicorn.api.controller.health_checkup

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
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/user/Insert_Deleted_User_Data.sql")
@Sql("/db/health_checkup/Insert_HealthCheckup_Data.sql")

class HealthCheckupPostTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when create health checkup`() {
        val healthCheckup = HealthCheckupPostRequest(
            bodyTemperature = 36.5,
            bloodPressure = "120/80",
            medicalRecord = "test",
            date = LocalDate.of(2021, 1, 1)
        )
        val userID = "test"

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/health_checkups").headers(HttpHeaders().apply{
            add("X-UID", userID)
        })
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(healthCheckup))
        )
        result.andExpect(status().isOk)
        result.andExpect(content().json(
            // language=json
            """
            {
                "userID": "${userID}",
                "bodyTemperature": ${healthCheckup.bodyTemperature},
                "bloodPressure": "${healthCheckup.bloodPressure}",
                "medicalRecord": "${healthCheckup.medicalRecord}",
                "date": "${healthCheckup.date}"
            }
            """.trimIndent()
        ))
    }

    @Test
    fun `should return 400 when user not found`() {
        val healthCheckup = HealthCheckupPostRequest(
            bodyTemperature = 36.5,
            bloodPressure = "120/80",
            medicalRecord = "test",
            date = LocalDate.of(2021, 1, 1)
        )
        val userID = "notfound"

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/health_checkups").headers(HttpHeaders().apply{
            add("X-UID", userID)
        })
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(healthCheckup))
        )
        result.andExpect(status().isBadRequest)
        result.andExpect(content().json(
            // language=json
            """
            {
                "errorType": "User not found"
            }
            """.trimIndent(), true))
    }
}
