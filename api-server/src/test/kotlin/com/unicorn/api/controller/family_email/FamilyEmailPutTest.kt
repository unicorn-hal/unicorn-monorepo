package com.unicorn.api.controller.family_email

import com.fasterxml.jackson.databind.ObjectMapper
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
@Sql("/db/user/Insert_Deleted_User_Data.sql")
@Sql("/db/family_email/Insert_FamilyEmail_Data.sql")
class FamilyEmailPutTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when update family email`() {
        val familyEmail =
            FamilyEmailPutRequest(
                email = "test@example.com",
                firstName = "John",
                lastName = "Doe",
                phoneNumber = "09012345678",
                iconImageUrl = "http://example.com/newicon.png",
            )

        val familyEmailID = "f47ac10b-58cc-4372-a567-0e02b2c3d470"
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/family_emails/$familyEmailID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(familyEmail)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "familyEmailID": "$familyEmailID",
                    "userID": "$userID",
                    "email": "${familyEmail.email}",
                    "firstName": "${familyEmail.firstName}",
                    "lastName": "${familyEmail.lastName}",
                    "phoneNumber": "${familyEmail.phoneNumber}",
                    "iconImageUrl": "${familyEmail.iconImageUrl}"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when family email not found`() {
        val familyEmail =
            FamilyEmailPutRequest(
                email = "test@example.com",
                firstName = "John",
                lastName = "Doe",
                phoneNumber = "09012345678",
                iconImageUrl = "http://example.com/newicon.png",
            )

        val familyEmailID = "f47ac10b-58cc-4372-a567-0e02b2c3d472"
        val userID = "test"
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/family_emails/$familyEmailID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(familyEmail)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                // language=json
                """
                {
                    "errorType": "Family email not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when user not found`() {
        val familyEmail =
            FamilyEmailPutRequest(
                email = "test@example.com",
                firstName = "John",
                lastName = "Doe",
                phoneNumber = "09012345678",
                iconImageUrl = "http://example.com/newicon.png",
            )

        val familyEmailID = "f47ac10b-58cc-4372-a567-0e02b2c3d470"
        val userID = "notfound"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/family_emails/$familyEmailID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(familyEmail)),
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
