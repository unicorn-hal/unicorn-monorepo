package com.unicorn.api.controller.medicine;

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
@Sql("/db/medicine/Insert_Medicine_Data.sql")
public class MedicinePutTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when medicine is updated`() {
        val medicine = MedicinePutRequest(
            medicineName = "test-medicine",
            quantity = 2
        )

        val medicineID = "123e4567-e89b-12d3-a456-426614174000"
        val userID = "test"

        val result = mockMvc.perform(
            MockMvcRequestBuilders.put("/medicines/${medicineID}").headers(HttpHeaders().apply {
                add("X-UID", userID)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicine))
        )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                    {
                        "medicineID": "$medicineID",
                        "medicineName": "${medicine.medicineName}",
                        "userID": "$userID",
                        "count": 50,
                        "quantity": ${medicine.quantity}
                    }
                """.trimIndent(), true
            )
        )
    }

    @Test
    fun `should return 400 when medicine ID is invalid`() {
        val medicine = MedicinePutRequest(
            medicineName = "test-medicine",
            quantity = 2
        )

        val invalidMedicineID = "invalid-id"
        val userID = "test"

        mockMvc.perform(
            MockMvcRequestBuilders.put("/medicines/$invalidMedicineID").headers(HttpHeaders().apply {
                add("X-UID", userID)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicine))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when quantity exceeds count`() {
        val medicine = MedicinePutRequest(
            medicineName = "test-medicine",
            quantity = 100
        )

        val userID = "test"
        val medicineID = "123e4567-e89b-12d3-a456-426614174000"

        mockMvc.perform(
            MockMvcRequestBuilders.put("/medicines/$medicineID").headers(HttpHeaders().apply {
                add("X-UID", userID)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicine))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when user is not found`() {
        val medicine = MedicinePutRequest(
            medicineName = "test-medicine",
            quantity = 2
        )

        val userID = "non-existent-user"
        val medicineID = "123e4567-e89b-12d3-a456-426614174000"

        mockMvc.perform(
            MockMvcRequestBuilders.put("/medicines/$medicineID").headers(HttpHeaders().apply {
                add("X-UID", userID)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicine))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when medicine is not found`() {
        val medicine = MedicinePutRequest(
            medicineName = "test-medicine",
            quantity = 2
        )

        val userID = "test"
        val nonExistentMedicineID = UUID.randomUUID().toString()

        mockMvc.perform(
            MockMvcRequestBuilders.put("/medicines/$nonExistentMedicineID").headers(HttpHeaders().apply {
                add("X-UID", userID)
            })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicine))
        )
            .andExpect(status().isBadRequest)
    }
}
