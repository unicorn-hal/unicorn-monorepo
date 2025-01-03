package com.unicorn.api.controller.chronic_disease

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
@Sql("/db/disease/Insert_Disease_Data.sql")
@Sql("/db/chronic_disease/Insert_Chronic_Disease_Data.sql")
class ChronicDiseaseGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `return 200 when get chronic_disease`() {
        val userID = "test"
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/chronic_diseases")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    ),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "data": [
                        {
                            "chronicDiseaseID": "f47ac10b-58cc-4372-a567-0e02b2c3d470",
                            "diseaseName": "風邪"
                        },
                        {
                            "chronicDiseaseID": "f47ac10b-58cc-4372-a567-0e02b2c3d469",
                            "diseaseName": "インフルエンザ"
                        },
                        {
                            "chronicDiseaseID": "f47ac10b-58cc-4372-a567-0e02b2c3d468",
                            "diseaseName": "花粉症"
                        }
                    ]
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `return 400 when user not found`() {
        val userID = "notfound"
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/chronic_diseases")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    ),
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
