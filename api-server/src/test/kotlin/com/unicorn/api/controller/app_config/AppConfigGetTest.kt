package com.unicorn.api.controller.app_config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
class AppConfigGetTest {
    @Autowired private lateinit var mockMvc: MockMvc

    @Test
    @Sql("/db/app_config/Insert_AppConfig_True.sql")
    fun `should return 200 with available true when config is present`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/app_config"))

        result.andExpect(status().isOk)
        result.andExpect(content().json("""{"available":true, releaseBuild: 5, demoMode: true}""", true))
    }

    @Test
    @Sql("/db/app_config/Insert_AppConfig_False.sql")
    fun `should return 200 with available false when config is present`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/app_config"))

        result.andExpect(status().isOk)
        result.andExpect(content().json("""{"available":false, releaseBuild: 4, demoMode: true}""", true))
    }

    @Test
    fun `should return 500 with errorType when config is not present`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/app_config"))

        result.andExpect(status().isInternalServerError)
        result.andExpect(
            content().json(
                """{"errorType":"serverError"}""",
                true,
            ),
        )
    }

    @Test
    @Sql("/db/app_config/Insert_AppConfig_True.sql")
    @Sql("/db/app_config/Insert_AppConfig_False.sql")
    fun `should return 500 with errorType when multiple configs are present`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/app_config"))

        result.andExpect(status().isInternalServerError)
        result.andExpect(content().json("""{"errorType":"serverError"}""", true))
    }
}
