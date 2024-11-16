package com.unicorn.api.query_service.medicine

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.unicorn.api.domain.medicine_reminders.DayOfWeek
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.time.LocalTime
import java.util.*

interface MedicineQueryService {
    fun getMedicines(uid: String): MedicineResult

    fun getMedicineReminders(
        reminderTime: LocalTime,
        reminderDayOfWeek: DayOfWeek,
    ): MedicineRemindersResult
}

@Service
class MedicineQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : MedicineQueryService {
    private fun String.toHHMM() = this.split(":").take(2).joinToString(":")

    override fun getMedicines(uid: String): MedicineResult {
        // language=postgresql
        val sql =
            """
            SELECT
                medicines.medicine_id,
                medicines.medicine_name,
                medicines.count,
                medicines.quantity,
                medicines.dosage,
                COALESCE(
                    json_agg(
                        json_build_object(
                            'reminderID', medicine_reminders.reminder_id,
                            'reminderTime', medicine_reminders.reminder_time,
                            'reminderDayOfWeek', medicine_reminders.day_of_week
                        )
                    ) FILTER (WHERE medicine_reminders.reminder_id IS NOT NULL),
                    '[]'
                ) AS reminders
            FROM medicines
            LEFT JOIN medicine_reminders ON medicines.medicine_id  = medicine_reminders.medicine_id
                AND medicine_reminders.deleted_at IS NULL
            WHERE 
                user_id = :userID
                AND medicines.deleted_at IS NULL
            GROUP BY medicines.medicine_id
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", uid)

        val medicines =
            namedParameterJdbcTemplate.query(
                sql,
                sqlParams,
                { rs, _ ->
                    val reminders =
                        jacksonObjectMapper().readValue<List<MedicineReminderDto>>(
                            rs.getString("reminders"),
                        )
                    val formatedReminders =
                        reminders.map {
                            it.copy(reminderTime = it.reminderTime.toHHMM())
                        }

                    MedicineDto(
                        medicineID = rs.getObject("medicine_id", UUID::class.java),
                        medicineName = rs.getString("medicine_name"),
                        count = rs.getInt("count"),
                        quantity = rs.getInt("quantity"),
                        dosage = rs.getInt("dosage"),
                        reminders = formatedReminders,
                    )
                },
            )

        return MedicineResult(medicines)
    }

    override fun getMedicineReminders(
        reminderTime: LocalTime,
        reminderDayOfWeek: DayOfWeek,
    ): MedicineRemindersResult {
        // language=postgresql
        val sql =
            """
            SELECT
                accounts.fcm_token_id
            FROM accounts
            INNER JOIN users u ON accounts.uid = u.user_id
            INNER JOIN medicines m ON u.user_id = m.user_id
            INNER JOIN medicine_reminders mr ON m.medicine_id = mr.medicine_id
            WHERE
                mr.reminder_time = :reminderTime
                AND :reminderDayOfWeek = ANY(mr.day_of_week)
                AND accounts.deleted_at IS NULL
                AND u.deleted_at IS NULL
                AND m.deleted_at IS NULL
                AND mr.deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("reminderTime", reminderTime)
                .addValue("reminderDayOfWeek", reminderDayOfWeek.toString())

        val reminders =
            namedParameterJdbcTemplate.query(
                sql,
                sqlParams,
            ) { rs, _ ->
                MedicineReminder(
                    fcmTokenId = rs.getString("fcm_token_id"),
                )
            }

        return MedicineRemindersResult(reminders)
    }
}

data class MedicineResult(
    val data: List<MedicineDto>,
)

data class MedicineDto(
    val medicineID: UUID,
    val medicineName: String,
    val count: Int,
    val quantity: Int,
    val dosage: Int,
    val reminders: List<MedicineReminderDto>,
)

data class MedicineReminderDto(
    val reminderID: UUID,
    val reminderTime: String,
    val reminderDayOfWeek: List<String>,
)

data class MedicineReminder(
    val fcmTokenId: String,
)

data class MedicineRemindersResult(
    val data: List<MedicineReminder>,
)
