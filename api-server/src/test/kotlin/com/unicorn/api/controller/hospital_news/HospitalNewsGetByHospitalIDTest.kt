package com.unicorn.api.controller.hospital_news

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
@Sql("/db/primary_doctor/Insert_Call_Support_Data.sql")
@Sql("/db/primary_doctor/Insert_Chat_Support_Data.sql")
@Sql("/db/hospital_news/Insert_HospitalNews_data.sql")
class HospitalNewsGetByHospitalIDTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when hospital news is called`() {
        val doctorID = "doctor"
        val hospitalID = "762a7a7e-41e4-46c2-b36c-f2b302cae3e7"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/hospitals/$hospitalID/news").headers(
                    HttpHeaders().apply {
                        add("X-UID", doctorID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": [
                        {
                            "hospitalNewsID": "b2c2aee5-8cb4-4ed3-b3ad-fb401c573de3",
                            "hospitalID": "$hospitalID",
                            "title": "内科杉山医院の新しいサービス開始",
                            "contents": "内科杉山医院では新たにオンライン診療サービスを開始しました。詳細はウェブサイトをご確認ください。",
                            "noticeImageUrl": "http://example.com/online_service.png",
                            "relatedUrl": "https://example.com/online-service",
                            "postedDate": "2024-11-30T23:00:00+09:00"
                        }
                    ]
                }    
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `returns multiple hospital news with status 200`() {
        val doctorID = "doctor"
        val hospitalID = "d8bfa31d-54b9-4c64-a499-6c522517e5f7"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/hospitals/$hospitalID/news").headers(
                    HttpHeaders().apply {
                        add("X-UID", doctorID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": [
                        {
                            "hospitalNewsID": "d1b2f9d0-9d9b-4625-bc38-54b75d3a0127",
                            "hospitalID": "$hospitalID",
                            "title": "新しい診療時間のお知らせ",
                            "contents": "新しい診療時間は月曜日から金曜日の午前9時から午後5時までです。",
                            "noticeImageUrl": "http://example.com/notice_image.png",
                            "relatedUrl": "https://example.com/new-schedule",
                            "postedDate": "2024-11-29T19:00:00+09:00"
                        },
                        {
                            "hospitalNewsID": "a3d94e60-d320-47ac-9cd1-5a7b204f6e8b",
                            "hospitalID": "$hospitalID",
                            "title": "重要なお知らせ：インフルエンザ予防接種",
                            "contents": "インフルエンザの予防接種を始めました。予約はお早めにお願いいたします。",
                            "noticeImageUrl": "http://example.com/vaccine_notice.png",
                            "relatedUrl": "https://example.com/vaccine",
                            "postedDate": "2024-11-28T18:30:00+09:00"
                        }
                    ]
                }    
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 400 when doctor not found`() {
        val doctorID = "not_found_doctor"
        val hospitalID = "d8bfa31d-54b9-4c64-a499-6c522517e5f7"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/hospitals/$hospitalID/news").headers(
                    HttpHeaders().apply {
                        add("X-UID", doctorID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when hospital not found`() {
        val doctorID = "doctor"
        val hospitalID = "c0bfa31d-54b9-4c64-a499-6c522517e5f7"

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/hospitals/$hospitalID/news").headers(
                    HttpHeaders().apply {
                        add("X-UID", doctorID)
                    },
                )
                    .contentType(MediaType.APPLICATION_JSON),
            )
        result.andExpect(status().isBadRequest)
    }
}
