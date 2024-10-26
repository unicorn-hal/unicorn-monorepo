package com.unicorn.api.controller.medicine

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

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/medicine/Insert_Medicine_Data.sql")
class MedicineDeleteTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 204 when medicine is deleted`() {
        val userID = "test"
        val medicineID = "123e4567-e89b-12d3-a456-426614174000"

        val result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/medicines/$medicineID").headers(HttpHeaders().apply {
                add("X-UID", userID)
            })
                .contentType(MediaType.APPLICATION_JSON)
        )

        result.andExpect(status().isNoContent)
    }

    @Test
    fun `should return 400 when user ID is invalid`() {
        val medicineID = "123e4567-e89b-12d3-a456-426614174001"
        val invalidUserID = "invalid-id"

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/medicines/$medicineID").headers(HttpHeaders().apply {
                add("X-UID", invalidUserID)
            })
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when medicine ID is invalid`() {
        val invalidMedicineID = "invalid-id"
        val userID = "test"

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/medicines/$invalidMedicineID").headers(HttpHeaders().apply {
                add("X-UID", userID)
            })
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }
}