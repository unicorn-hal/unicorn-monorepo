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
class HealthCheckupPutTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when update health checkup`() {
        val healthCheckupID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470")
        val userID = "test"
        val healthCheckupPutRequest =
            HealthCheckupPutRequest(
                bodyTemperature = 37.5,
                bloodPressure = "130/80",
                medicalRecord = "sample medical updated record",
                date = LocalDate.parse("2020-01-02"),
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .put("/health_checkups/$healthCheckupID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(healthCheckupPutRequest)),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "healthCheckupID": "$healthCheckupID",
                    "userID": "$userID",
                    "bodyTemperature": ${healthCheckupPutRequest.bodyTemperature},
                    "bloodPressure": "${healthCheckupPutRequest.bloodPressure}",
                    "medicalRecord": "${healthCheckupPutRequest.medicalRecord}",
                    "date": "${healthCheckupPutRequest.date}"
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 400 when user not found`() {
        val healthCheckupID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470")
        val userID = "notfound"
        val healthCheckupPutRequest =
            HealthCheckupPutRequest(
                bodyTemperature = 37.5,
                bloodPressure = "130/80",
                medicalRecord = "sample medical updated record",
                date = LocalDate.parse("2020-01-02"),
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .put("/health_checkups/$healthCheckupID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(healthCheckupPutRequest)),
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
    fun `should return 400 when health checkup not found`() {
        val healthCheckupID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d472")
        val userID = "test"
        val healthCheckupPutRequest =
            HealthCheckupPutRequest(
                bodyTemperature = 37.5,
                bloodPressure = "130/80",
                medicalRecord = "sample medical updated record",
                date = LocalDate.parse("2020-01-02"),
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .put("/health_checkups/$healthCheckupID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(healthCheckupPutRequest)),
            )
        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "errorType": "Health checkup not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when health checkup is deleted`() {
        val healthCheckupID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d471")
        val userID = "test"
        val healthCheckupPutRequest =
            HealthCheckupPutRequest(
                bodyTemperature = 37.5,
                bloodPressure = "130/80",
                medicalRecord = "sample medical updated record",
                date = LocalDate.parse("2020-01-02"),
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .put("/health_checkups/$healthCheckupID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(healthCheckupPutRequest)),
            )
        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "errorType": "Health checkup not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }
}
