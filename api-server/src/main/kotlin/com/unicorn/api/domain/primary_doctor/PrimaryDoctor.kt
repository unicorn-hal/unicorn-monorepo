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
                PrimaryDoctor.create(doctorID)
            }
            return PrimaryDoctors(userID, doctors)
        }

        fun fromStore(userID: UserID, existingDoctors: List<PrimaryDoctor>, doctorID: DoctorID): PrimaryDoctors {
            val newDoctor = PrimaryDoctor.create(doctorID)
            val updatedDoctors = existingDoctors + newDoctor
            return PrimaryDoctors(userID, updatedDoctors)
        }

        fun fromExistingDoctor(userID: UserID, existingDoctors: List<PrimaryDoctor>): PrimaryDoctors {
            return PrimaryDoctors(userID, existingDoctors)
        }
    }

    fun updateDoctors(newDoctorIDs: List<DoctorID>): Triple<List<PrimaryDoctor>, List<PrimaryDoctor>, List<PrimaryDoctor>> {
        val newDoctors = newDoctorIDs.map { doctorID ->
            doctors.find { it.doctorID == doctorID } ?: PrimaryDoctor.create(doctorID)
        }
        // 1. 共通する PrimaryDoctor
        val commonDoctors = doctors.filter { existingDoctor ->
            newDoctors.any { newDoctor -> newDoctor.doctorID == existingDoctor.doctorID }
        }

        // 2. 新たに追加する PrimaryDoctor
        val doctorsToAdd = newDoctors.filter { newDoctor ->
            doctors.none { existingDoctor -> existingDoctor.doctorID == newDoctor.doctorID }
        }

        // 3. 論理削除する PrimaryDoctor
        val doctorsToDelete = doctors.filter { existingDoctor ->
            newDoctors.none { newDoctor -> newDoctor.doctorID == existingDoctor.doctorID }
        }

        return Triple(commonDoctors, doctorsToAdd, doctorsToDelete)
    }
}

data class PrimaryDoctor private constructor(
    val primaryDoctorID: PrimaryDoctorID,
    val doctorID: DoctorID
) {
    companion object {
        fun create(doctorID: DoctorID): PrimaryDoctor {
            return PrimaryDoctor(primaryDoctorID = PrimaryDoctorID.newID(), doctorID = doctorID)
        }

        fun fromStore(primaryDoctorID: PrimaryDoctorID, doctorID: DoctorID): PrimaryDoctor {
            return PrimaryDoctor(primaryDoctorID = primaryDoctorID, doctorID = doctorID)
        }
    }
}

@JvmInline
value class PrimaryDoctorID(val value: UUID) {
    companion object {
        fun newID(): PrimaryDoctorID = PrimaryDoctorID(UUID.randomUUID())
    }
}
