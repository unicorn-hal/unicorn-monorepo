package com.unicorn.api.controller.medicine

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
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
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/medicine/Insert_Medicine_Data.sql")
@Sql("/db/medicine_reminder/Insert_Medicine_Reminder_Data.sql")
@Sql("/db/medicine_reminder/Insert_Deleted_Medicine_Reminder_Data.sql")
class MedicinesGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 with correct medicine data`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/medicines").headers(
                    HttpHeaders().apply {
                        add("X-UID", "test")
                    },
                ),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": [
                        {
                            "medicineID": "123e4567-e89b-12d3-a456-426614174000",
                            "medicineName": "Paracetamol",
                            "count": 50,
                            "quantity": 10,
                            "dosage": 1,
                            "reminders": [
                                {
                                    "reminderID": "123e4567-e89b-12d3-a456-426614174010",
                                    "reminderTime": "08:00",
                                    "reminderDayOfWeek": [
                                        "monday",
                                        "tuesday"
                                    ]
                                },
                                {
                                    "reminderID": "123e4567-e89b-12d3-a456-426614174011",
                                    "reminderTime": "12:00",
                                    "reminderDayOfWeek": [
                                        "wednesday",
                                        "thursday"
                                    ]
                                }
                            ]
                        },
                        {
                            "medicineID": "123e4567-e89b-12d3-a456-426614174001",
                            "medicineName": "Ibuprofen",
                            "count": 30,
                            "quantity": 15,
                            "dosage": 1,
                            "reminders": [
                                {
                                    "reminderID": "123e4567-e89b-12d3-a456-426614174012",
                                    "reminderTime": "16:00",
                                    "reminderDayOfWeek": [
                                        "friday",
                                        "saturday"
                                    ]
                                },
                                {
                                    "reminderID": "123e4567-e89b-12d3-a456-426614174013",
                                    "reminderTime": "20:00",
                                    "reminderDayOfWeek": [
                                        "sunday"
                                    ]
                                }
                            ]
                        },
                        {
                            "medicineID": "123e4567-e89b-12d3-a456-426614174002",
                            "medicineName": "Aspirin",
                            "count": 80,
                            "quantity": 20,
                            "dosage": 1,
                            "reminders": []
                        },
                        {
                            "medicineID": "123e4567-e89b-12d3-a456-426614174003",
                            "medicineName": "Amoxicillin",
                            "count": 20,
                            "quantity": 5,
                            "dosage": 3,
                            "reminders": []
                        }
                    ]
                }
                """.trimIndent(),
                true,
            ),
        )
    }

    @Test
    fun `should return 400 when uid is invalid`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/medicines").headers(
                    HttpHeaders().apply {
                        add("X-UID", "invalid-uid")
                    },
                ),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(content().string("User not found"))
    }
}
