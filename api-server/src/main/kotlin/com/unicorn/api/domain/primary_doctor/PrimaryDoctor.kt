package com.unicorn.api.domain.primary_doctor

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import java.util.*

data class PrimaryDoctor private constructor(
    val primaryDoctors: List<PrimaryDoctors>
) {
    companion object {
        fun create(
            primaryDoctors: List<PrimaryDoctors>
        ): PrimaryDoctor {
            return PrimaryDoctor(
                primaryDoctors = primaryDoctors
            )
        }

        fun fromStore(
            primaryDoctorID: UUID,
            userID: String,
            doctorID: String
        ): PrimaryDoctor {
            val primaryDoctors = PrimaryDoctors(
                primaryDoctorID = PrimaryDoctorID(primaryDoctorID),
                userID = UserID(userID),
                doctorID = DoctorID(doctorID)
            )
            return PrimaryDoctor(
                primaryDoctors = listOf(primaryDoctors)
            )
        }
    }

    fun update(
        newPrimaryDoctors: List<PrimaryDoctors>
    ): PrimaryDoctor {
        return this.copy(
            primaryDoctors = newPrimaryDoctors
        )
    }

    fun toResponse(): PrimaryDoctorResponse {
        return PrimaryDoctorResponse(
            userID = primaryDoctors.first().userID,
            doctorIDs = primaryDoctors.map { it.doctorID }
        )
    }
}

data class PrimaryDoctors(
    val primaryDoctorID: PrimaryDoctorID,
    val userID: UserID,
    val doctorID: DoctorID
)

data class PrimaryDoctorResponse(
    val userID: UserID,
    val doctorIDs: List<DoctorID>
)

@JvmInline
value class PrimaryDoctorID(val value: UUID) {
    init {
        require(value.toString().isNotEmpty()) { "PrimaryDoctorID must not be empty" }
    }
}