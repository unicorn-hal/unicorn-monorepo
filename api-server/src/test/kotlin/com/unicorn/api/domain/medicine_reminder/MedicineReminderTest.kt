package com.unicorn.api.domain.medicine_reminder

import com.unicorn.api.domain.medicine.MedicineID
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalTime
import java.util.*
import kotlin.test.assertEquals

class MedicineReminderTest {
    @Nested
    inner class MedicineReminderCheck {
        @Test
        fun `should create reminder`() {
            val reminderID = UUID.randomUUID()
            val reminderTime = LocalTime.of(10, 0)
            val dayOfWeek = listOf("monday")

            val medicineReminder =
                MedicineReminder.of(
                    reminderID = reminderID,
                    reminderTime = reminderTime,
                    dayOfWeek = dayOfWeek,
                )

            assertEquals(reminderID, medicineReminder.reminderID.value)
            assertEquals(reminderTime, medicineReminder.reminderTime.value)
            assertEquals(dayOfWeek, medicineReminder.dayOfWeek.map { it.name })
        }

        @Test
        fun `should create reminder from store`() {
            val reminderID = UUID.randomUUID()
            val reminderTime = LocalTime.of(10, 0)
            val dayOfWeek = setOf("monday")

            val medicineReminder =
                MedicineReminder.fromStore(
                    reminderID = reminderID,
                    reminderTime = reminderTime,
                    dayOfWeek = dayOfWeek,
                )

            assertEquals(reminderID, medicineReminder.reminderID.value)
            assertEquals(reminderTime, medicineReminder.reminderTime.value)
            assertEquals(dayOfWeek, medicineReminder.dayOfWeek.map { it.name }.toSet())
        }

        @Test
        fun `should update reminder`() {
            val reminderID = UUID.randomUUID()
            val reminderTime = LocalTime.of(10, 0)
            val dayOfWeek = listOf("monday")

            val medicineReminder =
                MedicineReminder.of(
                    reminderID = reminderID,
                    reminderTime = reminderTime,
                    dayOfWeek = dayOfWeek,
                )

            val newReminderTime = LocalTime.of(11, 0)
            val newDayOfWeek = listOf("tuesday")

            val updatedMedicineReminder =
                medicineReminder.update(
                    reminderTime = ReminderTime(newReminderTime),
                    dayOfWeek = newDayOfWeek.map { DayOfWeek.valueOf(it) }.toSet(),
                )

            assertEquals(reminderID, updatedMedicineReminder.reminderID.value)
            assertEquals(newReminderTime, updatedMedicineReminder.reminderTime.value)
            assertEquals(newDayOfWeek, updatedMedicineReminder.dayOfWeek.map { it.name })
        }
    }

    @Nested
    inner class MedicineRemindersCheck {
        @Test
        fun `should create medicine reminders`() {
            val medicineID = MedicineID(UUID.randomUUID())
            val reminders =
                listOf(
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(10, 0),
                        dayOfWeek = listOf("monday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(11, 0),
                        dayOfWeek = listOf("tuesday"),
                    ),
                )

            val medicineReminders =
                MedicineReminders.of(
                    medicineID = medicineID,
                    reminders = reminders,
                )

            assertEquals(medicineID, medicineReminders.medicineID)
            assertEquals(reminders, medicineReminders.reminders)
        }

        @Test
        fun `should update medicine reminders`() {
            val medicineID = MedicineID(UUID.randomUUID())
            val reminders =
                listOf(
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(10, 0),
                        dayOfWeek = listOf("monday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(11, 0),
                        dayOfWeek = listOf("tuesday"),
                    ),
                )

            val medicineReminders =
                MedicineReminders.of(
                    medicineID = medicineID,
                    reminders = reminders,
                )

            val newReminders =
                listOf(
                    MedicineReminder.of(
                        reminderID = reminders[0].reminderID.value,
                        reminderTime = LocalTime.of(12, 0),
                        dayOfWeek = listOf("wednesday", "thursday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(13, 0),
                        dayOfWeek = listOf("thursday"),
                    ),
                )

            val updatedMedicineReminders =
                medicineReminders.update(
                    reminders = newReminders,
                )

            assertEquals(medicineID, updatedMedicineReminders.medicineID)
            assertEquals(newReminders, updatedMedicineReminders.reminders)
        }

        @Test
        fun `should not create medicine reminders when reminders count is more than 5`() {
            val medicineID = MedicineID(UUID.randomUUID())
            val reminders =
                listOf(
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(10, 0),
                        dayOfWeek = listOf("monday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(11, 0),
                        dayOfWeek = listOf("tuesday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(12, 0),
                        dayOfWeek = listOf("wednesday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(13, 0),
                        dayOfWeek = listOf("thursday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(14, 0),
                        dayOfWeek = listOf("friday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(15, 0),
                        dayOfWeek = listOf("saturday"),
                    ),
                )

            val exception =
                assertThrows(IllegalArgumentException::class.java) {
                    MedicineReminders.of(
                        medicineID = medicineID,
                        reminders = reminders,
                    )
                }

            assertEquals("reminders should be 5 or fewer", exception.message)
        }

        @Test
        fun `should not update medicine reminders when reminders count is more than 5`() {
            val medicineID = MedicineID(UUID.randomUUID())
            val reminders =
                listOf(
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(10, 0),
                        dayOfWeek = listOf("monday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(11, 0),
                        dayOfWeek = listOf("tuesday"),
                    ),
                )
            val medicineReminders =
                MedicineReminders.of(
                    medicineID = medicineID,
                    reminders = reminders,
                )
            val newReminders =
                listOf(
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(12, 0),
                        dayOfWeek = listOf("wednesday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(13, 0),
                        dayOfWeek = listOf("thursday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(14, 0),
                        dayOfWeek = listOf("friday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(15, 0),
                        dayOfWeek = listOf("saturday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(16, 0),
                        dayOfWeek = listOf("sunday"),
                    ),
                    MedicineReminder.of(
                        reminderID = UUID.randomUUID(),
                        reminderTime = LocalTime.of(17, 0),
                        dayOfWeek = listOf("monday"),
                    ),
                )

            val exception =
                assertThrows(IllegalArgumentException::class.java) {
                    medicineReminders.update(
                        reminders = newReminders,
                    )
                }

            assertEquals("reminders should be 5 or fewer", exception.message)
        }
    }
}
