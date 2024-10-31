package com.unicorn.api.controller.user

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
import java.time.LocalDate

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/user/Insert_Deleted_User_Data.sql")
class UserPutTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when user is updated`() {
        val user =
            UserPutRequest(
                firstName = "firstNameTest",
                lastName = "lastNameTest",
                email = "email@sample.com",
                birthDate = LocalDate.of(2000, 1, 1),
                gender = "male",
                address = "address",
                postalCode = "0000000",
                phoneNumber = "00000000000",
                iconImageUrl = "iconImageUrl",
                bodyHeight = 180.0,
                bodyWeight = 75.0,
                occupation = "occupation",
            )

        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/users/$userID").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "userID": "$userID",
                    "firstName": "${user.firstName}",
                    "lastName": "${user.lastName}",
                    "email": "${user.email}",
                    "birthDate": "${user.birthDate}",
                    "gender": "${user.gender}",
                    "address": "${user.address}",
                    "postalCode": "${user.postalCode}",
                    "phoneNumber": "${user.phoneNumber}",
                    "iconImageUrl": "${user.iconImageUrl}",
                    "bodyHeight": ${user.bodyHeight},
                    "bodyWeight": ${user.bodyWeight},
                    "occupation": "${user.occupation}"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 404 when user is not found`() {
        val user =
            UserPutRequest(
                firstName = "firstName",
                lastName = "lastName",
                email = "email@sample.com",
                birthDate = LocalDate.of(2000, 1, 1),
                gender = "male",
                address = "address",
                postalCode = "0000000",
                phoneNumber = "00000000000",
                iconImageUrl = "iconImageUrl",
                bodyHeight = 180.0,
                bodyWeight = 75.0,
                occupation = "occupation",
            )

        val userID = "notfound"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/users/$userID").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user)),
            )
        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "User not found"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when request is invalid`() {
        val user =
            UserPostRequest(
                firstName = "firstName",
                lastName = "lastName",
                email = "invalid",
                birthDate = LocalDate.of(2000, 1, 1),
                gender = "male",
                address = "address",
                postalCode = "0000000",
                phoneNumber = "00000000000",
                iconImageUrl = "iconImageUrl",
                bodyHeight = 180.0,
                bodyWeight = 75.0,
                occupation = "occupation",
            )
        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.put("/users/$userID").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user)),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                    "errorType": "email should be valid"
                }
                """.trimIndent(),
                true,
            ),
        )
    }
}
