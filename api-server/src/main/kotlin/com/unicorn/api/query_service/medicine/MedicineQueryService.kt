package com.unicorn.api.query_service.medicine

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

interface MedicineQueryService {
    fun getMedicines(uid: String): MedicineResult
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
            WHERE 
                user_id = :userID
                AND medicines.deleted_at IS NULL
                AND medicine_reminders.deleted_at IS NULL
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
