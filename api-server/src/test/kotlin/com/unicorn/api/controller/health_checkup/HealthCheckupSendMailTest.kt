package com.unicorn.api.controller.health_checkup

import com.fasterxml.jackson.databind.ObjectMapper
import com.unicorn.api.util.MailTransport
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
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
@Sql("/db/primary_doctor/Insert_Parent_Account_Data.sql")
@Sql("/db/primary_doctor/Insert_User_Data.sql")
@Sql("/db/primary_doctor/Insert_Hospital_Data.sql")
@Sql("/db/primary_doctor/Insert_Department_Data.sql")
@Sql("/db/primary_doctor/Insert_Doctor_Data.sql")
@Sql("/db/primary_doctor/Insert_Doctor_Department_Data.sql")
@Sql("/db/primary_doctor/Insert_PrimaryDoctor_Data.sql")
@Sql("/db/primary_doctor/Insert_Call_Support_Data.sql")
@Sql("/db/primary_doctor/Insert_Chat_Support_Data.sql")
@Sql("/db/health_checkup/Insert_HealthCheckup_Data.sql")
class HealthCheckupSendMailTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var mailTransport: MailTransport

    @Test
    fun `should return 200 when create health checkup and send mail`() {
        doNothing().`when`(mailTransport).send(anyString(), anyString(), anyString())

        val healthCheckup =
            HealthCheckupPostRequest(
                bodyTemperature = 36.5,
                bloodPressure = "120/80",
                medicalRecord = "test",
                date = LocalDate.of(2021, 1, 1),
            )
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/health_checkups").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(healthCheckup)),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "userID": "$userID",
                    "bodyTemperature": ${healthCheckup.bodyTemperature},
                    "bloodPressure": "${healthCheckup.bloodPressure}",
                    "medicalRecord": "${healthCheckup.medicalRecord}",
                    "date": "${healthCheckup.date}"
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 400 when user not found`() {
        val healthCheckup =
            HealthCheckupPostRequest(
                bodyTemperature = 36.5,
                bloodPressure = "120/80",
                medicalRecord = "test",
                date = LocalDate.of(2021, 1, 1),
            )
        val userID = "notfound"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/health_checkups").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(healthCheckup)),
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
}
