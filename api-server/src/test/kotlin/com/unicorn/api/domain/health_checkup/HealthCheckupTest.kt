package com.unicorn.api.domain.health_checkup

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class HealthCheckupTest {
    @Test
    fun `should create health checkup`() {
        val healthCheckup = HealthCheckup.create(
            userID = "test",
            bodyTemperature = 36.5,
            bloodPressure = "120/80",
            medicalRecord = "test",
            date = "2021-01-01"
        )

        assertEquals("test", healthCheckup.userID.value)
        assertEquals(36.5, healthCheckup.bodyTemperature.value)
        assertEquals("120/80", healthCheckup.bloodPressure.value)
        assertEquals("test", healthCheckup.medicalRecord.value)
        assertEquals("2021-01-01", healthCheckup.date.value)
    }

    @Test
    fun `should create health checkup from store`() {
        val healthCheckup = HealthCheckup.fromStore(
            healthCheckupID = UUID.randomUUID(),
            userID = "test",
            bodyTemperature = 36.5,
            bloodPressure = "120/80",
            medicalRecord = "test",
            date = "2021-01-01"
        )

        assertEquals("test", healthCheckup.userID.value)
        assertEquals(36.5, healthCheckup.bodyTemperature.value)
        assertEquals("120/80", healthCheckup.bloodPressure.value)
        assertEquals("test", healthCheckup.medicalRecord.value)
        assertEquals("2021-01-01", healthCheckup.date.value)
    }

    @Test
    fun `should update health checkup`() {
        val healthCheckup = HealthCheckup.create(
            userID = "test",
            bodyTemperature = 36.5,
            bloodPressure = "120/80",
            medicalRecord = "test",
            date = "2021-01-01"
        )

        val updatedHealthCheckup = healthCheckup.update(
            bodyTemperature = BodyTemperature(37.5),
            bloodPressure = BloodPressure("130/90"),
            medicalRecord = MedicalRecord("updated test"),
            date = CheckupedDate("2021-01-02")
        )

        assertEquals(37.5, updatedHealthCheckup.bodyTemperature.value)
        assertEquals("130/90", updatedHealthCheckup.bloodPressure.value)
        assertEquals("updated test", updatedHealthCheckup.medicalRecord.value)
        assertEquals("2021-01-02", updatedHealthCheckup.date.value)
    }

    @Test
    fun `should return an error message when null body temperature`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            HealthCheckup.create(
                userID = "test",
                bodyTemperature = 0.0,
                bloodPressure = "120/80",
                medicalRecord = "test",
                date = "2021-01-01"
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
                date = "2021-01-01"
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
                date = "2021-01-01"
            )
        }
        assertEquals("medical record should not be blank", exception.message)
    }

    @Test
    fun `should return an error message when null date`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            HealthCheckup.create(
                userID = "test",
                bodyTemperature = 36.5,
                bloodPressure = "120/80",
                medicalRecord = "test",
                date = ""
            )
        }
        assertEquals("date should not be blank", exception.message)
    }
}