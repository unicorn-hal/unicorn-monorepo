package com.unicorn.api.infrastructure.medicine_reminders

import com.unicorn.api.domain.medicine.MedicineID
import com.unicorn.api.domain.medicine_reminders.MedicineReminder
import com.unicorn.api.domain.medicine_reminders.MedicineReminders
import com.unicorn.api.domain.user.User
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface MedicineRemindersRepository {
    fun store(medicineReminders: MedicineReminders): MedicineReminders

    fun getBy(medicineID: MedicineID): MedicineReminders

    fun delete(medicineReminders: MedicineReminders): Unit

    fun deleteByUser(user: User)
}

@Repository
class MedicineRemindersRepositoryImpl(
    val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : MedicineRemindersRepository {
    override fun store(medicineReminders: MedicineReminders): MedicineReminders {
        // language=postgresql
        val deleteSql =
            """
            UPDATE medicine_reminders
                SET deleted_at = NOW()
            WHERE medicine_id = :medicineID
            """.trimIndent()

        // language=postgresql
        val sql =
            """
            INSERT INTO medicine_reminders (
                reminder_id,
                medicine_id,
                reminder_time,
                day_of_week,
                created_at
            ) VALUES (
                :reminderID,
                :medicineID,
                :reminderTime,
                :dayOfWeek,
                NOW()
            ) 
            ON CONFLICT (reminder_id) DO UPDATE 
            SET
                reminder_time = EXCLUDED.reminder_time,
                day_of_week = EXCLUDED.day_of_week,
                deleted_at = NULL
            WHERE medicine_reminders.created_at IS NOT NULL;
            """.trimIndent()

        val deleteSqlParams =
            MapSqlParameterSource()
                .addValue("medicineID", medicineReminders.medicineID.value)

        val sqlParams =
            medicineReminders.reminders.map { reminder ->
                MapSqlParameterSource()
                    .addValue("reminderID", reminder.reminderID.value)
                    .addValue("medicineID", medicineReminders.medicineID.value)
                    .addValue("reminderTime", reminder.reminderTime.value)
                    .addValue("dayOfWeek", reminder.dayOfWeek.map { it.name }.toTypedArray())
            }

        namedParameterJdbcTemplate.update(deleteSql, deleteSqlParams)
        namedParameterJdbcTemplate.batchUpdate(sql, sqlParams.toTypedArray())

        return medicineReminders
    }

    override fun getBy(medicineID: MedicineID): MedicineReminders {
        // language=postgresql
        val sql =
            """
            SELECT
                reminder_id,
                medicine_id,
                reminder_time,
                day_of_week
            FROM medicine_reminders
            WHERE medicine_id = :medicineID
                AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("medicineID", medicineID.value)

        val reminders =
            namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                MedicineReminder.fromStore(
                    reminderID = rs.getObject("reminder_id", UUID::class.java),
                    reminderTime = rs.getTime("reminder_time").toLocalTime(),
                    dayOfWeek =
                        (rs.getArray("day_of_week").array as Array<*>)
                            .map { it.toString() }
                            .toSet(),
                )
            }

        return MedicineReminders.of(medicineID, reminders)
    }

    override fun delete(medicineReminders: MedicineReminders) {
        // language=postgresql
        val sql =
            """
            UPDATE medicine_reminders
                SET deleted_at = NOW()
            WHERE medicine_id = :medicineID
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("medicineID", medicineReminders.medicineID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }

    override fun deleteByUser(user: User) {
        // language=postgresql
        val sql =
            """
            UPDATE medicine_reminders
            SET deleted_at = NOW()
            WHERE medicine_id IN (
                SELECT medicine_id 
                FROM medicines 
                WHERE user_id = :userID
            )
            AND deleted_at IS NULL;
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", user.userID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
