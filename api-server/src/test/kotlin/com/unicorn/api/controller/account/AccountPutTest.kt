package com.unicorn.api.controller.account

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
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

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/db/account/Insert_Account_Data.sql")
@Sql("/db/account/Insert_Deleted_Account_Data.sql")
class AccountPutTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when account is updated`() {
        val account =
            AccountPutRequest(
                fcmTokenId = "fcmTokenId",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/accounts").headers(
                    HttpHeaders().apply {
                        add("X-UID", "test")
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(account)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "fcmTokenId": "${account.fcmTokenId}"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 404 when account is not found`() {
        val account =
            AccountPutRequest(
                fcmTokenId = "fcmTokenId",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/accounts").headers(
                    HttpHeaders().apply {
                        add("X-UID", "invalid")
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(account)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "account not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }
}
