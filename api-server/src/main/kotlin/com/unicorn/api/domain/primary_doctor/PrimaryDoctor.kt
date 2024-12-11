package com.unicorn.api.domain.primary_doctor

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import java.util.*

data class PrimaryDoctor private constructor(
    val primaryDoctorID: PrimaryDoctorID,
    val userID: UserID,
    val doctorID: DoctorID,
) {
    companion object {
        fun fromStore(
            primaryDoctorID: UUID,
            userID: String,
            doctorID: String,
        ): PrimaryDoctor {
            return PrimaryDoctor(
                primaryDoctorID = PrimaryDoctorID(primaryDoctorID),
                userID = UserID(userID),
                doctorID = DoctorID(doctorID),
            )
        }

        fun create(
            userID: String,
            doctorID: String,
        ): PrimaryDoctor {
            return PrimaryDoctor(
                primaryDoctorID = PrimaryDoctorID(UUID.randomUUID()),
                userID = UserID(userID),
                doctorID = DoctorID(doctorID),
            )
        }
    }
}

@JvmInline
value class PrimaryDoctorID(val value: UUID)
