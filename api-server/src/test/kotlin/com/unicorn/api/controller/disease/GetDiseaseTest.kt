package com.unicorn.api.controller.disease

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
@Sql("/db/disease/Insert_Disease_Data.sql")
class GetDiseaseTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when disease is called by diseaseName`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/diseases").headers(
                    HttpHeaders().apply {
                        add("X-UID", "test")
                    },
                ).param("diseaseName", "風邪"),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": [
                        {
                            diseaseID: 1,
                            diseaseName: "風邪"
                        }
                    ]
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 200 when disease is called by empty diseaseName`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/diseases").headers(
                    HttpHeaders().apply {
                        add("X-UID", "test")
                    },
                ).param("diseaseName", ""),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": []
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 200 when disease is called by diseaseRuby`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/diseases").headers(
                    HttpHeaders().apply {
                        add("X-UID", "test")
                    },
                ).param("diseaseName", "かぜ"),
            )
        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                {
                    "data": [
                        {
                            diseaseID: 1,
                            diseaseName: "風邪"
                        }
                    ]
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 200 when disease is called by famous disease`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/diseases/famous").headers(
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
                          "diseaseID":14,
                          "diseaseName":"歯周病"
                        },
                        {
                          "diseaseID":16,
                          "diseaseName":"虫歯"
                        }
                    ]
                }
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `should return 400 when disease is called by diseaseName is not passed`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/diseases").headers(
                    HttpHeaders().apply {
                        add("X-UID", "test")
                    },
                ),
            )
        result.andExpect(status().isBadRequest)
    }
}
