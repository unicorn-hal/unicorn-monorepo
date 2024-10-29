package com.unicorn.api.infrastructure.health_checkup

import com.unicorn.api.domain.health_checkup.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/health_checkup/Insert_HealthCheckup_Data.sql")

class HealthCheckupRepositoryTest {
    @Autowired
    private lateinit var healthCheckupRepository: HealthCheckupRepository
    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findHealthCheckupByID(healthCheckupID: UUID): HealthCheckup? {
        //language=postgresql
        val sql = """
            SELECT
                health_checkup_id,
                checkuped_user_id,
                body_temperature,
                blood_pressure,
                medical_record,
                checkuped_date
            FROM health_checkups
            WHERE health_checkup_id = :healthCheckupID
            AND deleted_at IS NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("healthCheckupID", healthCheckupID)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            HealthCheckup.fromStore(
                healthCheckupID = UUID.fromString(rs.getString("health_checkup_id")),
                userID = rs.getString("checkuped_user_id"),
                bodyTemperature = rs.getDouble("body_temperature"),
                bloodPressure = rs.getString("blood_pressure"),
                medicalRecord = rs.getString("medical_record"),
                date = rs.getDate("checkuped_date").toLocalDate()
            )
        }.singleOrNull()
    }

    @Test
    fun `should store health checkup`() {
        val healthCheckup = HealthCheckup.create(
            userID = "test",
            bodyTemperature = 36.5,
            bloodPressure = "120/80",
            medicalRecord = "test",
            date = LocalDate.parse("2021-01-01")
        )
        healthCheckupRepository.store(healthCheckup)
        val storedHealthCheckup = findHealthCheckupByID(healthCheckup.healthCheckupID.value)
        assertEquals(healthCheckup.userID, storedHealthCheckup?.userID)
        assertEquals(healthCheckup.bodyTemperature, storedHealthCheckup?.bodyTemperature)
        assertEquals(healthCheckup.bloodPressure, storedHealthCheckup?.bloodPressure)
        assertEquals(healthCheckup.medicalRecord, storedHealthCheckup?.medicalRecord)
        assertEquals(healthCheckup.date, storedHealthCheckup?.date)
    }

    @Test
    fun `should update health checkup`() {
        val healthCheckup = HealthCheckup.fromStore(
            healthCheckupID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470"),
            userID = "test",
            bodyTemperature = 36.5,
            bloodPressure = "120/80",
            medicalRecord = "sample medical record",
            date = LocalDate.parse("2021-01-01")
        )

        val updatedHealthCheckup = healthCheckup.update(
            bodyTemperature = BodyTemperature(37.5),
            bloodPressure = BloodPressure("130/90"),
            medicalRecord = MedicalRecord("updated medical record"),
            date = CheckupedDate(LocalDate.parse("2021-01-02"))
        )

        healthCheckupRepository.store(updatedHealthCheckup)

        val storedHealthCheckup = findHealthCheckupByID(healthCheckup.healthCheckupID.value)
        assertEquals(healthCheckup.healthCheckupID, storedHealthCheckup?.healthCheckupID)
        assertEquals(healthCheckup.userID, storedHealthCheckup?.userID)
        assertEquals(updatedHealthCheckup.bodyTemperature, storedHealthCheckup?.bodyTemperature)
        assertEquals(updatedHealthCheckup.bloodPressure, storedHealthCheckup?.bloodPressure)
        assertEquals(updatedHealthCheckup.medicalRecord, storedHealthCheckup?.medicalRecord)
        assertEquals(updatedHealthCheckup.date, storedHealthCheckup?.date)
    }

    @Test
    fun `should find health checkup by ID`() {
        val healthCheckupID = HealthCheckupID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470"))
        val foundHealthCheckup = healthCheckupRepository.getOrNullBy(healthCheckupID)
        assertNotNull(foundHealthCheckup)
        assertEquals(healthCheckupID, foundHealthCheckup!!.healthCheckupID)
        assertEquals("test", foundHealthCheckup.userID.value)
        assertEquals(36.5, foundHealthCheckup.bodyTemperature.value)
        assertEquals("120/80", foundHealthCheckup.bloodPressure.value)
        assertEquals("sample medical record", foundHealthCheckup.medicalRecord.value)
        assertEquals(LocalDate.parse("2020-01-01"), foundHealthCheckup.date.value)
    }

    @Test
    fun `should not be found if deleted_at is not NULL`() {
        val healthCheckupID = HealthCheckupID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d471"))
        val foundHealthCheckup = healthCheckupRepository.getOrNullBy(healthCheckupID)
        assertNull(foundHealthCheckup)
    }

    @Test
    fun ` should return null when health checkup does not exist`() {
        val healthCheckupID = HealthCheckupID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d472"))
        val foundHealthCheckup = healthCheckupRepository.getOrNullBy(healthCheckupID)
        assertNull(foundHealthCheckup)
    }

    @Test
    fun `Should delete health checkup`() {
        val healthCheckup = HealthCheckup.create(
            userID = "test",
            bodyTemperature = 36.5,
            bloodPressure = "120/80",
            medicalRecord = "test",
            date = LocalDate.parse("2021-01-01")
        )

        healthCheckupRepository.store(healthCheckup)
        healthCheckupRepository.delete(healthCheckup)
        val foundHealthCheckup = healthCheckupRepository.getOrNullBy(healthCheckup.healthCheckupID)
        assertNull(foundHealthCheckup)
    }

}