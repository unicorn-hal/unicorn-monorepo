package com.unicorn.api.infrastructure.medicine_reminders

import com.unicorn.api.domain.medicine.MedicineID
import com.unicorn.api.domain.medicine_reminders.MedicineReminder
import com.unicorn.api.domain.medicine_reminders.MedicineReminders
import com.unicorn.api.domain.user.User
import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/medicine/Insert_Medicine_Data.sql")
@Sql("/db/medicine_reminder/Insert_Medicine_Reminder_Data.sql")
class MedicineRemindersRepositoryTest {
    @Autowired
    private lateinit var medicineRemindersRepository: MedicineRemindersRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun getBy(medicineID: MedicineID): MedicineReminders? {
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

    private fun getByUserID(userID: UserID): List<MedicineReminder> {
        // language=postgresql
        val sql =
            """
            SELECT 
                mr.reminder_id,
                mr.medicine_id,
                mr.reminder_time,
                mr.day_of_week
            FROM medicine_reminders mr
            INNER JOIN medicines m ON mr.medicine_id = m.medicine_id
            WHERE m.user_id = :userID
              AND mr.deleted_at IS NULL
              AND m.deleted_at IS NULL;
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", userID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            MedicineReminder.fromStore(
                reminderID = rs.getObject("reminder_id", UUID::class.java),
                reminderTime = rs.getTime("reminder_time").toLocalTime(),
                dayOfWeek =
                    (rs.getArray("day_of_week").array as Array<*>)
                        .map { it.toString() }
                        .toSet(),
            )
        }
    }

    @Test
    fun `should store medicine reminders`() {
        val medicineID = MedicineID(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
        val reminders =
            listOf(
                MedicineReminder.of(
                    reminderID = UUID.fromString("00000000-0000-0000-0000-000000000001"),
                    reminderTime = LocalTime.of(8, 0),
                    dayOfWeek = listOf("monday", "wednesday", "friday"),
                ),
                MedicineReminder.of(
                    reminderID = UUID.fromString("00000000-0000-0000-0000-000000000002"),
                    reminderTime = LocalTime.of(12, 0),
                    dayOfWeek = listOf("tuesday", "thursday"),
                ),
            )
        val medicineReminders = MedicineReminders.of(medicineID, reminders)

        medicineRemindersRepository.store(medicineReminders)

        val storedMedicineReminders = getBy(medicineID)
        assertEquals(medicineReminders, storedMedicineReminders)
    }

    @Test
    fun `should store empty medicine reminders`() {
        val medicineID = MedicineID(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"))
        val reminders = emptyList<MedicineReminder>()
        val medicineReminders = MedicineReminders.of(medicineID, reminders)

        medicineRemindersRepository.store(medicineReminders)

        val storedMedicineReminders = getBy(medicineID)
        assertEquals(medicineReminders, storedMedicineReminders)
    }

    @Test
    fun `should update medicine reminders`() {
        val medicineID = MedicineID(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"))
        val reminders =
            listOf(
                MedicineReminder.of(
                    reminderID = UUID.fromString("123e4567-e89b-12d3-a456-426614174012"),
                    reminderTime = LocalTime.of(8, 0),
                    dayOfWeek = listOf("monday", "wednesday", "friday"),
                ),
            )
        val medicineReminders = MedicineReminders.of(medicineID, reminders)

        medicineRemindersRepository.store(medicineReminders)

        val storedMedicineReminders = getBy(medicineID)
        assertEquals(medicineReminders, storedMedicineReminders)
    }

    @Test
    fun `should get medicine reminders`() {
        val medicineID = MedicineID(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"))
        val reminders =
            listOf(
                MedicineReminder.of(
                    reminderID = UUID.fromString("123e4567-e89b-12d3-a456-426614174012"),
                    reminderTime = LocalTime.of(16, 0),
                    dayOfWeek = listOf("friday", "saturday"),
                ),
                MedicineReminder.of(
                    reminderID = UUID.fromString("123e4567-e89b-12d3-a456-426614174013"),
                    reminderTime = LocalTime.of(20, 0),
                    dayOfWeek = listOf("sunday"),
                ),
            )
        val medicineReminders = MedicineReminders.of(medicineID, reminders)

        val storedMedicineReminders = medicineRemindersRepository.getBy(medicineID)
        assertEquals(medicineReminders, storedMedicineReminders)
    }

    @Test
    fun `should get empty medicine reminders`() {
        val medicineID = MedicineID(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"))
        val reminders = emptyList<MedicineReminder>()
        val medicineReminders = MedicineReminders.of(medicineID, reminders)

        val storedMedicineReminders = medicineRemindersRepository.getBy(medicineID)
        assertEquals(medicineReminders, storedMedicineReminders)
    }

    @Test
    fun `should delete medicine reminders`() {
        val medicineID = MedicineID(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
        val reminders =
            listOf(
                MedicineReminder.of(
                    reminderID = UUID.fromString("123e4567-e89b-12d3-a456-426614174013"),
                    reminderTime = LocalTime.of(8, 0),
                    dayOfWeek = listOf("monday", "wednesday", "friday"),
                ),
            )
        val medicineReminders = MedicineReminders.of(medicineID, reminders)

        medicineRemindersRepository.delete(medicineReminders)

        val deletedMedicineReminders = getBy(medicineID)
        assertEquals(MedicineReminders.of(medicineID, emptyList()), deletedMedicineReminders)
    }

    @Test
    fun `should delete medicine reminders by user`() {
        val user =
            User.fromStore(
                userID = "test",
                firstName = "test",
                lastName = "test",
                email = "sample@test.com",
                birthDate = LocalDate.of(1990, 1, 1),
                gender = "male",
                address = "test",
                postalCode = "0000000",
                phoneNumber = "00000000000",
                iconImageUrl = "https://example.com",
                bodyHeight = 170.4,
                bodyWeight = 60.4,
                occupation = "test",
            )

        medicineRemindersRepository.deleteByUser(user)

        val deletedMedicineReminders = getByUserID(user.userID)
        assertEquals(0, deletedMedicineReminders.size)
    }
}
