package com.unicorn.api.application_service.family_email

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
class FamilyEmailDeleteTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 204 when family email is deleted`() {
        val familyEmailID = "f47ac10b-58cc-4372-a567-0e02b2c3d470"
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/family_emails/$familyEmailID")
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
    fun `should return 400 when family email not found`() {
        val familyEmailID = "f47ac10b-58cc-4372-a567-0e02b2c3d472"
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/family_emails/$familyEmailID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when user not found`() {
        val familyEmailID = "f47ac10b-58cc-4372-a567-0e02b2c3d470"
        val userID = "notfound"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/family_emails/$familyEmailID")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", userID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isBadRequest)
    }
}
