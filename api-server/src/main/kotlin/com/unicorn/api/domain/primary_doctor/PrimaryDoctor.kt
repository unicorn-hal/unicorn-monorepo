package com.unicorn.api.domain.primary_doctor

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import java.util.*

data class PrimaryDoctors private constructor(
    val userID: UserID,
    val doctors: List<PrimaryDoctor>
) {
    companion object {
        fun create(userID: UserID, doctorIDs: List<DoctorID> = emptyList()): PrimaryDoctors {
            val doctors = doctorIDs.map { doctorID ->
                PrimaryDoctor(primaryDoctorID = PrimaryDoctorID.newID(), doctorID = doctorID)
            }
            return PrimaryDoctors(userID, doctors)
        }
    }

    fun fromStore(doctorID: DoctorID): PrimaryDoctors {
        val newDoctor = PrimaryDoctor(primaryDoctorID = PrimaryDoctorID.newID(), doctorID = doctorID)
        return this.copy(doctors = this.doctors + newDoctor)
    }

    fun update(newDoctors: List<PrimaryDoctor>): PrimaryDoctors {
        return this.copy(doctors = newDoctors)
    }
}

data class PrimaryDoctor(
    val primaryDoctorID: PrimaryDoctorID,
    val doctorID: DoctorID
)

@JvmInline
value class PrimaryDoctorID(val value: UUID) {
    companion object {
        fun newID(): PrimaryDoctorID = PrimaryDoctorID(UUID.randomUUID())
    }
}