package com.unicorn.api.controller.hospital

import com.unicorn.api.application.hospital.HospitalDto
import com.unicorn.api.application.hospital.HospitalQueryService
import com.unicorn.api.application.hospital.HospitalResult
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import org.mockito.Mockito
import java.util.*

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class HospitalsGetTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var hospitalQueryService: HospitalQueryService

    @Test
    fun `should return 200 when hospitals are called`() {
        val hospitalIDString = "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        val hospitalID = UUID.fromString(hospitalIDString)
        val hospitalDto = HospitalDto(
            hospitalID = hospitalID,
            hospitalName = "Test Hospital",
            address = "123 Test St",
            postalCode = "123-4567",
            phoneNumber = "123-456-7890"
        )

        // HospitalResultのインスタンスを作成してモックに返す
        val hospitalResult = HospitalResult(hospitals = listOf(hospitalDto))
        Mockito.`when`(hospitalQueryService.getHospitals()).thenReturn(hospitalResult)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/hospitals").headers(HttpHeaders().apply {
            add("X-UID", "test-uid")
        }))

        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            {
                "hospitals": [
                    {
                        "hospitalID": "$hospitalIDString",
                        "hospitalName": "Test Hospital",
                        "address": "123 Test St",
                        "postalCode": "123-4567",
                        "phoneNumber": "123-456-7890"
                    }
                ]
            }
        """.trimIndent(), true))
    }

    @Test
    fun `should return 500 when an internal server error occurs`() {
        Mockito.`when`(hospitalQueryService.getHospitals()).thenThrow(RuntimeException("Internal Server Error"))

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/hospitals").headers(HttpHeaders().apply {
            add("X-UID", "test-uid")
        }))

        result.andExpect(status().isInternalServerError)
    }
}
