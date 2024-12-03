package com.unicorn.api.controller.medicine

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
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/notification/Insert_Notification_Data.sql")
@Sql("/db/medicine/Insert_Medicine_Data.sql")
@Sql("/db/medicine_reminder/Insert_Medicine_Reminder_Data.sql")
@Sql("/db/medicine_reminder/Insert_Deleted_Medicine_Reminder_Data.sql")
class MedicineReminderGetTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 200 with correct medicine reminder data`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/medicines/reminders")
                    .param("reminderTime", "08:00")
                    .param("reminderDayOfWeek", "monday"),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                    {
                        "data": [
                            {
                                "fcmTokenId": "fcm_token_id"
                            }
                        ]
                    }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 200 when there is no medicine reminder data`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/medicines/reminders")
                    .param("reminderTime", "08:00")
                    .param("reminderDayOfWeek", "sunday"),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                    {
                        "data": []
                    }
                """,
                true,
            ),
        )
    }

    // 削除されているリマインダーは取得できない
    @Test
    fun `should return 200 when there is no deleted medicine reminder data`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/medicines/reminders")
                    .param("reminderTime", "08:00")
                    .param("reminderDayOfWeek", "wednesday"),
            )

        result.andExpect(status().isOk)
        result.andExpect(
            content().json(
                """
                    {
                        "data": []
                    }
                """,
                true,
            ),
        )
    }

    @Test
    fun `should return 400 with invalid day of week`() {
        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/medicines/reminders")
                    .param("reminderTime", "08:00")
                    .param("reminderDayOfWeek", "invalid_day"),
            )

        result.andExpect(status().isBadRequest)
        result.andExpect(
            content().json(
                """
                    {
                        "errorType": "Bad Request"
                    }
                """,
                true,
            ),
        )
    }
}
