package com.unicorn.api.controller.doctor

import com.fasterxml.jackson.databind.ObjectMapper
import com.unicorn.api.domain.doctor.DoctorID
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

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/doctor/Insert_Parent_Account_Data.sql")
@Sql("/db/hospital/Insert_Hospital_Data.sql")
@Sql("/db/department/Insert_Department_Data.sql")
@Sql("/db/doctor/Insert_Doctor_Data.sql")
@Sql("/db/doctor_department/Insert_Doctor_Department_Data.sql")
@Sql("/db/chat_support/Insert_Chat_Support_Data.sql")
@Sql("/db/call_support/Insert_Call_Support_Data.sql")
class DoctorDeleteTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 204 when doctor is deleted`() {
        val doctorID = DoctorID("doctor")

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/doctors/${doctorID.value}").headers(
                    HttpHeaders().apply {
                        add("X-UID", doctorID.value)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )

        result.andExpect(status().isNoContent)
    }

    @Test
    fun `should return 400 when doctor is not found`() {
        val doctorID = DoctorID("not_found_doctor")

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/doctors/${doctorID.value}").headers(
                    HttpHeaders().apply {
                        add("X-UID", doctorID.value)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                {
                  "errorType": "Doctor not found"
                }
                """.trimIndent(),
            ),
        )
    }
}
