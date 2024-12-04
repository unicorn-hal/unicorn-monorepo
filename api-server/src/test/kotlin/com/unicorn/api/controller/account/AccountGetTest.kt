package com.unicorn.api.controller.account

import com.unicorn.api.query_service.account.AccountDto
import org.junit.jupiter.api.Nested
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
@Sql("/db/hospital/Insert_Hospital_Data.sql")
@Sql("/db/department/Insert_Department_Data.sql")
@Sql("/db/doctor/Insert_Doctor_Data.sql")
@Sql("/db/doctor_department/Insert_Doctor_Department_Data.sql")
@Sql("/db/chat_support/Insert_Chat_Support_Data.sql")
@Sql("/db/call_support/Insert_Call_Support_Data.sql")
class AccountGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when account is found`() {
        val accountDto =
            AccountDto(
                uid = "test",
                role = "user",
                fcmTokenId = "fcm_token_id",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/accounts")
                    .header("X-UID", accountDto.uid),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "uid": "${accountDto.uid}",
                    "role": "${accountDto.role}",
                    "fcmTokenId": "${accountDto.fcmTokenId}"
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 404 when account is not found`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/accounts")
                    .header("X-UID", "invalid"),
            )

        result.andExpect(status().isNotFound)
    }

    @Test
    fun `should return 404 when account is deleted`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/accounts")
                    .header("X-UID", "test2"),
            )

        result.andExpect(status().isNotFound)
    }

    @Nested
    inner class GetByUID {
        @Test
        fun `should return 200 when account is found`() {
            val accountDto =
                AccountDto(
                    uid = "test",
                    role = "user",
                    fcmTokenId = "fcm_token_id",
                )

            val result =
                mockMvc.perform(
                    MockMvcRequestBuilders.get("/accounts/${accountDto.uid}")
                        .header("X-UID", "doctor"),
                )

            result.andExpect(status().isOk)
            result.andExpect(
                content().json(
                    """
                    {
                        "uid": "${accountDto.uid}",
                        "role": "${accountDto.role}",
                        "fcmTokenId": "${accountDto.fcmTokenId}"
                    }
                    """.trimIndent(),
                ),
            )
        }

        @Test
        fun `should return 400 when account is not doctor`() {
            val result =
                mockMvc.perform(
                    MockMvcRequestBuilders.get("/accounts/test")
                        .header("X-UID", "test"),
                )

            result.andExpect(status().isBadRequest)
            result.andExpect(
                content().json(
                    """
                    {
                        "errorType": "account is not doctor"
                    }
                    """.trimIndent(),
                ),
            )
        }

        @Test
        fun `should return 400 when account is not found`() {
            val result =
                mockMvc.perform(
                    MockMvcRequestBuilders.get("/accounts/invalid")
                        .header("X-UID", "doctor"),
                )

            result.andExpect(status().isBadRequest)
            result.andExpect(
                content().json(
                    """
                    {
                        "errorType": "account is not found"
                    }
                    """.trimIndent(),
                ),
            )
        }
    }
}
