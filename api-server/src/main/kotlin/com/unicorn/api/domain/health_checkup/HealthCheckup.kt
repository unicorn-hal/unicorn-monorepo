package com.unicorn.api.domain.health_checkup

import com.unicorn.api.domain.user.UserID
import java.time.LocalDate
import java.util.*

data class HealthCheckup private constructor(
    val healthCheckupID: HealthCheckupID,
    val userID: UserID,
    val bodyTemperature: BodyTemperature,
    val bloodPressure: BloodPressure,
    val medicalRecord: MedicalRecord,
    val date: CheckupedDate,
) {
    companion object {
        fun fromStore(
            healthCheckupID: UUID,
            userID: String,
            bodyTemperature: Double,
            bloodPressure: String,
            medicalRecord: String,
            date: LocalDate,
        ): HealthCheckup  {
            return HealthCheckup(
                healthCheckupID = HealthCheckupID(healthCheckupID),
                userID = UserID(userID),
                bodyTemperature = BodyTemperature(bodyTemperature),
                bloodPressure = BloodPressure(bloodPressure),
                medicalRecord = MedicalRecord(medicalRecord),
                date = CheckupedDate(date),
            )
        }

        fun create(
            userID: String,
            bodyTemperature: Double,
            bloodPressure: String,
            medicalRecord: String,
            date: LocalDate,
        ): HealthCheckup {
            return HealthCheckup(
                healthCheckupID = HealthCheckupID(UUID.randomUUID()),
                userID = UserID(userID),
                bodyTemperature = BodyTemperature(bodyTemperature),
                bloodPressure = BloodPressure(bloodPressure),
                medicalRecord = MedicalRecord(medicalRecord),
                date = CheckupedDate(date),
            )
        }
    }

    fun update(
        bodyTemperature: BodyTemperature,
        bloodPressure: BloodPressure,
        medicalRecord: MedicalRecord,
        date: CheckupedDate,
    ): HealthCheckup {
        return this.copy(
            bodyTemperature = bodyTemperature,
            bloodPressure = bloodPressure,
            medicalRecord = medicalRecord,
            date = date,
        )
    }
}

@JvmInline
value class HealthCheckupID(val value: UUID)

@JvmInline
value class BodyTemperature(val value: Double) {
    init {
        require(value > 0) { "body temperature should be greater than 0" }
    }
}

@JvmInline
value class BloodPressure(val value: String) {
    init {
        require(value.isNotBlank()) { "blood pressure should not be blank" }
    }
}

@JvmInline
value class MedicalRecord(val value: String) {
    init {
        require(value.isNotBlank()) { "medical record should not be blank" }
    }
}

@JvmInline
value class CheckupedDate(val value: LocalDate)
