package com.unicorn.api.domain.health_checkup

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

class HealthCheckupTest {
    @Test
    fun `should create health checkup`() {
        val healthCheckup = HealthCheckup.create(
            userID = "test",
            bodyTemperature = 36.5,
            bloodPressure = "120/80",
            medicalRecord = "test",
            date = LocalDate.of(2021, 1, 1)
        )

        assertEquals("test", healthCheckup.userID.value)
        assertEquals(36.5, healthCheckup.bodyTemperature.value)
        assertEquals("120/80", healthCheckup.bloodPressure.value)
        assertEquals("test", healthCheckup.medicalRecord.value)
        assertEquals(LocalDate.of(2021, 1, 1), healthCheckup.date.value)
    }

    @Test
    fun `should create health checkup from store`() {
        val healthCheckup = HealthCheckup.fromStore(
            healthCheckupID = UUID.randomUUID(),
            userID = "test",
            bodyTemperature = 36.5,
            bloodPressure = "120/80",
            medicalRecord = "test",
            date = LocalDate.of(2021, 1, 1)
        )

        assertEquals("test", healthCheckup.userID.value)
        assertEquals(36.5, healthCheckup.bodyTemperature.value)
        assertEquals("120/80", healthCheckup.bloodPressure.value)
        assertEquals("test", healthCheckup.medicalRecord.value)
        assertEquals(LocalDate.of(2021, 1, 1), healthCheckup.date.value)
    }

    @Test
    fun `should update health checkup`() {
        val healthCheckup = HealthCheckup.create(
            userID = "test",
            bodyTemperature = 36.5,
            bloodPressure = "120/80",
            medicalRecord = "test",
            date = LocalDate.of(2021, 1, 1)
        )

        val updatedHealthCheckup = healthCheckup.update(
            bodyTemperature = BodyTemperature(37.5),
            bloodPressure = BloodPressure("130/90"),
            medicalRecord = MedicalRecord("updated test"),
            date = CheckupedDate(LocalDate.of(2021, 1, 2))
        )

        assertEquals(37.5, updatedHealthCheckup.bodyTemperature.value)
        assertEquals("130/90", updatedHealthCheckup.bloodPressure.value)
        assertEquals("updated test", updatedHealthCheckup.medicalRecord.value)
        assertEquals(LocalDate.of(2021, 1, 2), updatedHealthCheckup.date.value)
    }

    @Test
    fun `should return an error message when null body temperature`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            HealthCheckup.create(
                userID = "test",
                bodyTemperature = 0.0,
                bloodPressure = "120/80",
                medicalRecord = "test",
                date = LocalDate.of(2021, 1, 1)
            )
        }
        assertEquals("body temperature should be greater than 0", exception.message)
    }

    @Test
    fun `should return an error message when null blood pressure`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            HealthCheckup.create(
                userID = "test",
                bodyTemperature = 36.5,
                bloodPressure = "",
                medicalRecord = "test",
                date = LocalDate.of(2021, 1, 1)
            )
        }
        assertEquals("blood pressure should not be blank", exception.message)
    }

    @Test
    fun `should return an error message when null medical record`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            HealthCheckup.create(
                userID = "test",
                bodyTemperature = 36.5,
                bloodPressure = "120/80",
                medicalRecord = "",
                date = LocalDate.of(2021, 1, 1)
            )
        }
        assertEquals("medical record should not be blank", exception.message)
    }
}