package com.unicorn.api.controller.departments

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
@Sql("/db/primary_doctor/Insert_Parent_Account_Data.sql")
@Sql("/db/primary_doctor/Insert_User_Data.sql")
@Sql("/db/primary_doctor/Insert_Hospital_Data.sql")
@Sql("/db/primary_doctor/Insert_Department_Data.sql")
@Sql("/db/primary_doctor/Insert_Doctor_Data.sql")
@Sql("/db/primary_doctor/Insert_Doctor_Department_Data.sql")
@Sql("/db/primary_doctor/Insert_PrimaryDoctor_Data.sql")
class DeleteDoctorDepartmentsTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 204 when delete doctor departments`() {
        val doctorID = "doctor"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/doctors/$doctorID/doctor_departments")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", doctorID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isNoContent)
    }

    @Test
    fun `should return 400 when doctor not found`() {
        val doctorID = "not_found"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/doctors/$doctorID/doctor_departments")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", doctorID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isBadRequest)
    }
}
