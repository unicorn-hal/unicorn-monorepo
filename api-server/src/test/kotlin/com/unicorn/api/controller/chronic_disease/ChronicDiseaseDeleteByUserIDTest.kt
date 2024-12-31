package com.unicorn.api.controller.chronic_disease

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
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/disease/Insert_Disease_Data.sql")
@Sql("/db/chronic_disease/Insert_Chronic_Disease_Data.sql")
class ChronicDiseaseDeleteByUserIDTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 204 when delete success`() {
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/$userID/chronic_diseases")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isNoContent)
    }

    @Test
    fun `should return 400 when user not found`() {
        val userID = "notfound"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/$userID/chronic_diseases")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
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
