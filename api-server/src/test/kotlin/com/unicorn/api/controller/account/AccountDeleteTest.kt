package com.unicorn.api.controller.account

import com.unicorn.api.domain.account.Account
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/db/account/Insert_Account_Data.sql")
@Sql("/db/account/Insert_Deleted_Account_Data.sql")
class AccountDeleteTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 204 when account is deleted`() {
        val account =
            Account.fromStore(
                uid = "test",
                role = "user",
                fcmTokenId = "fcm_token_id",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/accounts")
                    .header("X-UID", account.uid.value),
            )

        result.andExpect(status().isNoContent)
    }

    @Test
    fun `should return 404 when account is not found`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/accounts")
                    .header("X-UID", "invalid"),
            )

        result.andExpect(status().isNotFound)
    }

    @Test
    fun `should return 404 when account is already deleted`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/accounts")
                    .header("X-UID", "test2"),
            )

        result.andExpect(status().isNotFound)
    }
}
