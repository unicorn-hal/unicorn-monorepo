package com.unicorn.api.controller.account

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountPostTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when account is created`() {
        val account = AccountPostRequest(
            uid = "uid",
            role = "user",
            fcmTokenId = "fcmTokenId"
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts").headers(HttpHeaders().apply {
                add("X-UID", "uid")
            })
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(account)))

        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            {
                "uid": "${account.uid}",
                "role": "${account.role}",
                "fcmTokenId": "${account.fcmTokenId}"
            }
        """.trimIndent()))
    }

    @Test
    fun `should return 400 when role is invalid`() {
        val account = AccountPostRequest(
            uid = "uid",
            role = "invalid",
            fcmTokenId = "fcmTokenId"
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts").headers(HttpHeaders().apply {
                add("X-UID", "uid")
            })
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(account)))

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when uid is not provided`() {
        val account = AccountPostRequest(
            uid = "",
            role = "user",
            fcmTokenId = "fcmTokenId"
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/accounts").headers(HttpHeaders().apply {
                add("X-UID", "uid")
            })
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(account)))

        result.andExpect(status().isBadRequest)
    }
}