package com.unicorn.api.controller.medicine

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
import java.time.LocalTime
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/medicine/Insert_Medicine_Data.sql")
class MedicinePostTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when medicine is created`() {
        val medicineRequest =
            MedicinePostRequest(
                medicineName = "medicineName",
                count = 10,
                quantity = 5,
                dosage = 3,
                reminders =
                    listOf(
                        ReminderRequest(
                            reminderID = UUID.fromString("123e4567-e89b-12d3-a456-426614174010"),
                            reminderTime = LocalTime.of(8, 0, 0),
                            reminderDayOfWeek = listOf("monday", "tuesday"),
                        ),
                    ),
            )

        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/medicines").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(medicineRequest)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "medicineName": "${medicineRequest.medicineName}",
                    "count": ${medicineRequest.count},
                    "quantity": ${medicineRequest.quantity},
                    "dosage": ${medicineRequest.dosage}
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 400 when medicineName is missing`() {
        val medicineRequest =
            MedicinePostRequest(
                medicineName = "",
                count = 10,
                quantity = 5,
                dosage = 3,
                reminders = emptyList(),
            )

        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/medicines").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(medicineRequest)),
            )

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when count is negative`() {
        val medicineRequest =
            MedicinePostRequest(
                medicineName = "medicineName",
                count = -5,
                quantity = 5,
                dosage = 3,
                reminders = emptyList(),
            )

        val userID = "test"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/medicines").headers(
                    HttpHeaders().apply {
                        add("X-UID", userID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(medicineRequest)),
            )

        result.andExpect(status().isBadRequest)
    }
}
