package com.unicorn.api.controller.departments

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
// import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import org.springframework.test.context.jdbc.Sql

// @ActiveProfiles("test")
@TestPropertySource(locations=["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/db/department/Insert_Department_Data.sql")
class DepartmentsGetTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 when departments is called`(){
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/departments").headers(HttpHeaders().apply {
            add("X-UID", "a1dcb69e-472f-4a57-90a2-f2c63b62ec90")
        }))
        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            {
                "data": [
                    {
                        departmentID: "a1dcb69e-472f-4a57-90a2-f2c63b62ec90",
                        departmentName: "総合内科"
                    },
                    {   
                        departmentID: "b68a87a3-b7f1-4b85-b0ab-6c620d68d791",
                        departmentName: "循環器内科"
                    },
                    {   
                        departmentID: "cd273b1b-0c3b-4b89-b2b9-01b21832b44c",
                        departmentName: "呼吸器内科"
                    },
                    {   
                        departmentID: "df3f2f1d-0c32-44d9-a429-5260e62e81aa",
                        departmentName: "消化器内科"
                    },
                    {   
                        departmentID: "ed8fb319-798f-4b47-bcf3-2a8f6486e38d",
                        departmentName: "腎臓・内分泌内科"
                    }
                ]
            }
        """.trimIndent(), true))
    }
}
