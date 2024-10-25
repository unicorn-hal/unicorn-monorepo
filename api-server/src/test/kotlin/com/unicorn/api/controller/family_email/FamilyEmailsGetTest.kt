package com.unicorn.api.controller.family_email

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import org.springframework.test.context.jdbc.Sql

@TestPropertySource(locations=["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/family_email/Insert_FamilyEmail_Data.sql")
class FamilyEmailsGetTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when familyEmails is called`(){
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/family_emails").headers(HttpHeaders().apply {
            add("X-UID", "test")
        }))
        result.andExpect(status().isOk)
        result.andExpect(content().json(
            // language=json
            """
            {
                "data": [
                    {
                        "familyEmailID": "f47ac10b-58cc-4372-a567-0e02b2c3d470",
                        "email": "sample@sample.com",
                        "familyFirstName": "太郎",
                        "familyLastName": "山田",
                        "phoneNumber": "09012345678",
                        "iconImageUrl": "https://example.com"
                    }
                ]
            }
            """.trimIndent(), true))
    }

    @Test
    fun `should return 404 when user is not found`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/family_emails").headers(HttpHeaders().apply {
            add("X-UID", "notFound")
        }))
        result.andExpect(status().isNotFound)
        result.andExpect(content().json(
            // language=json
            """
            {
                "errorType": "User not found"
            }
            """.trimIndent(), true))
    }
}