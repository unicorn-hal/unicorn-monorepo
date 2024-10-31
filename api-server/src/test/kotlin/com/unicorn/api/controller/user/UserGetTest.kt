package com.unicorn.api.controller.user

import com.unicorn.api.domain.user.User
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
class UserGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when user is found`() {
        val user =
            User.fromStore(
                userID = "test",
                firstName = "test",
                lastName = "test",
                email = "sample@test.com",
                birthDate = LocalDate.of(1990, 1, 1),
                gender = "male",
                address = "test",
                postalCode = "0000000",
                phoneNumber = "00000000000",
                iconImageUrl = "https://example.com",
                bodyHeight = 170.4,
                bodyWeight = 60.4,
                occupation = "test",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users/${user.userID.value}").headers(
                    HttpHeaders().apply {
                        add("X-UID", user.userID.value)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "userID": "${user.userID.value}",
                    "firstName": "${user.firstName.value}",
                    "lastName": "${user.lastName.value}",
                    "email": "${user.email.value}",
                    "birthDate": "${user.birthDate.value}",
                    "gender": "${user.gender}",
                    "address": "${user.address.value}",
                    "postalCode": "${user.postalCode.value}",
                    "phoneNumber": "${user.phoneNumber.value}",
                    "iconImageUrl": "${user.iconImageUrl?.value}",
                    "bodyHeight": ${user.bodyHeight.value},
                    "bodyWeight": ${user.bodyWeight.value},
                    "occupation": "${user.occupation.value}"
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 404 when user is not found`() {
        val userID = "notFound"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users/$userID").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )

        result.andExpect(status().isNotFound)
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
}
