package com.unicorn.api.controller.account

import com.unicorn.api.query_service.account.AccountDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
class AccountGetTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when account is found`() {
        val accountDto = AccountDto(
            uid = "test",
            role = "user",
            fcmTokenId = "fcm_token_id"
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts")
            .header("X-UID", accountDto.uid))

        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            {
                "uid": "${accountDto.uid}",
                "role": "${accountDto.role}",
                "fcmTokenId": "${accountDto.fcmTokenId}"
            }
        """.trimIndent()))
    }

    @Test
    fun `should return 404 when account is not found`() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts")
            .header("X-UID", "invalid"))

        result.andExpect(status().isNotFound)
    }

    @Test
    fun `should return 404 when account is deleted`() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/accounts")
            .header("X-UID", "test2"))

        result.andExpect(status().isNotFound)
    }
}