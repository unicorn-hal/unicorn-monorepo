package com.unicorn.api.domain.medicine_reminder

import com.unicorn.api.domain.medicine.MedicineID
import java.time.LocalTime
import java.util.*

data class MedicineReminders private constructor(
    val medicineID: MedicineID,
    val reminders: List<MedicineReminder>,
) {
    companion object {
        const val MAX_REMINDERS = 5

        fun of(
            medicineID: MedicineID,
            reminders: List<MedicineReminder>,
        ): MedicineReminders {
            require(reminders.count() <= MAX_REMINDERS) {
                "reminders should be 5 or fewer"
            }

            return MedicineReminders(
                medicineID = medicineID,
                reminders = reminders,
            )
        }

        fun fromStore(
            medicineID: UUID,
            reminders: List<MedicineReminder>,
        ): MedicineReminders {
            return MedicineReminders(
                medicineID = MedicineID(medicineID),
                reminders = reminders,
            )
        }
    }

    fun update(reminders: List<MedicineReminder>): MedicineReminders {
        require(reminders.count() <= MAX_REMINDERS) {
            "reminders should be 5 or fewer"
        }

        // すでに存在するreminderを更新し、なければ新しいreminderを追加する
        val newReminders =
            reminders.map { reminder ->
                this.reminders.find { it.reminderID == reminder.reminderID }?.update(
                    reminder.reminderTime,
                    reminder.dayOfWeek,
                ) ?: reminder
            }

        return this.copy(reminders = newReminders)
    }
}

data class MedicineReminder private constructor(
    val reminderID: ReminderID,
    val reminderTime: ReminderTime,
    val dayOfWeek: Set<DayOfWeek>,
) {
    companion object {
        fun of(
            reminderID: UUID,
            reminderTime: LocalTime,
            dayOfWeek: List<String>,
        ): MedicineReminder {
            return MedicineReminder(
                reminderID = ReminderID(reminderID),
                reminderTime = ReminderTime(reminderTime),
                dayOfWeek = dayOfWeek.map { DayOfWeek.valueOf(it) }.toSet(),
            )
        }

        fun fromStore(
            reminderID: UUID,
            reminderTime: LocalTime,
            dayOfWeek: Set<String>,
        ): MedicineReminder {
            return MedicineReminder(
                reminderID = ReminderID(reminderID),
                reminderTime = ReminderTime(reminderTime),
                dayOfWeek = dayOfWeek.map { DayOfWeek.valueOf(it) }.toSet(),
            )
        }
    }

    fun update(
        reminderTime: ReminderTime,
        dayOfWeek: Set<DayOfWeek>,
    ): MedicineReminder {
        return this.copy(
            reminderTime = reminderTime,
            dayOfWeek = dayOfWeek,
        )
    }
}

@JvmInline
value class ReminderID(val value: UUID)

@JvmInline
value class ReminderTime(val value: LocalTime)

enum class DayOfWeek {
    sunday,
    monday,
    tuesday,
    wednesday,
    thursday,
    friday,
    saturday,
}
