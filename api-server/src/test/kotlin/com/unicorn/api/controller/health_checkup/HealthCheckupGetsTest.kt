package com.unicorn.api.controller.health_checkup

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/health_checkup/Insert_HealthCheckup_Data.sql")
class HealthCheckupGetsTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should 200 when get all health checkup`() {
        val userID = "test"
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .get("/health_checkups")
                    .headers(HttpHeaders().apply { add("X-UID", userID) }),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "data": [
                        {
                            "healthCheckupID": "f47ac10b-58cc-4372-a567-0e02b2c3d470",
                            "userID": "$userID",
                            "bodyTemperature": 36.5,
                            "bloodPressure": "120/80",
                            "medicalRecord": "sample medical record",
                            "date": "2020-01-01"
                        }
                    ]
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should 400 when user not found`() {
        val userID = "notfound"
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .get("/health_checkups")
                    .headers(HttpHeaders().apply { add("X-UID", userID) }),
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
