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
class MedicinesGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 with correct medicine data`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/medicines").headers(HttpHeaders().apply {
            add("X-UID", "test-uid")
        }))

        result.andExpect(status().isOk)
        result.andExpect(
            content().json("""
            {
                "data": [
                    {
                        "medicineID": "123e4567-e89b-12d3-a456-426614174000",
                        "medicineName": "Paracetamol",
                        "count": 5,
                        "quantity": 10
                    },
                    {
                        "medicineID": "123e4567-e89b-12d3-a456-426614174001",
                        "medicineName": "Ibuprofen",
                        "count": 3,
                        "quantity": 15
                    },
                    {
                        "medicineID": "123e4567-e89b-12d3-a456-426614174002",
                        "medicineName": "Aspirin",
                        "count": 8,
                        "quantity": 20
                    },
                    {
                        "medicineID": "123e4567-e89b-12d3-a456-426614174003",
                        "medicineName": "Amoxicillin",
                        "count": 2,
                        "quantity": 5
                    }
                ]
            }
        """.trimIndent(), true))
    }

}