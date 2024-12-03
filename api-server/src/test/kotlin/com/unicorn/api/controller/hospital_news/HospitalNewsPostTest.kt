package com.unicorn.api.controller.hospital_news

import com.fasterxml.jackson.databind.ObjectMapper
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
import kotlin.test.Test

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/primary_doctor/Insert_Parent_Account_Data.sql")
@Sql("/db/primary_doctor/Insert_User_Data.sql")
@Sql("/db/primary_doctor/Insert_Hospital_Data.sql")
@Sql("/db/primary_doctor/Insert_Department_Data.sql")
@Sql("/db/primary_doctor/Insert_Doctor_Data.sql")
@Sql("/db/primary_doctor/Insert_Doctor_Department_Data.sql")
@Sql("/db/call_support/Insert_Call_Support_Data.sql")
@Sql("/db/hospital_news/Insert_HospitalNews_data.sql")
class HospitalNewsPostTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when hospital news created`() {
        val doctorID = "doctor"
        val hospitalID = "d8bfa31d-54b9-4c64-a499-6c522517e5f7"

        val hospitalNews =
            HospitalNewsRequest(
                hospitalID = UUID.fromString(hospitalID),
                title = "テストタイトル",
                contents = "これはテストコンテンツです。これはテストコンテンツです。",
                noticeImageUrl = "http://example.com/online_service.jpg",
                relatedUrl = "https://example.com/store",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/hospitals/$hospitalID/news")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", doctorID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(hospitalNews)),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "hospitalID": "$hospitalID",
                    "title": "${hospitalNews.title}",
                    "contents": "${hospitalNews.contents}",
                    "noticeImageUrl": "${hospitalNews.noticeImageUrl}",
                    "relatedUrl": "${hospitalNews.relatedUrl}"
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 400 when doctor not found`() {
        val doctorID = "not_found_doctor"
        val hospitalID = "d8bfa31d-54b9-4c64-a499-6c522517e5f7"

        val hospitalNews =
            HospitalNewsRequest(
                hospitalID = UUID.fromString(hospitalID),
                title = "テストタイトル",
                contents = "これはテストコンテンツです。これはテストコンテンツです。",
                noticeImageUrl = "http://example.com/online_service.jpg",
                relatedUrl = "https://example.com/store",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/hospitals/$hospitalID/news")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", doctorID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(hospitalNews)),
            )
        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when hospital not found`() {
        val doctorID = "doctor"
        val hospitalID = "d8bfa31d-54b9-4c64-a499-6c522517e0a0"

        val hospitalNews =
            HospitalNewsRequest(
                hospitalID = UUID.fromString(hospitalID),
                title = "テストタイトル",
                contents = "これはテストコンテンツです。これはテストコンテンツです。",
                noticeImageUrl = "http://example.com/online_service.jpg",
                relatedUrl = "https://example.com/store",
            )

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/hospitals/$hospitalID/news")
                    .headers(
                        HttpHeaders().apply {
                            add("X-UID", doctorID)
                        },
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(hospitalNews)),
            )
        result.andExpect(status().isBadRequest)
    }
}
