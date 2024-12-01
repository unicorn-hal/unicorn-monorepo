package com.unicorn.api.domain.emergency

import com.unicorn.api.domain.user.UserID
import java.util.*

data class Emergency private constructor(
    val emergencyID: EmergencyID,
    val userID: UserID,
    val userLatitude: UserLatitude,
    val userLongitude: UserLongitude,
) {
    companion object {
        fun fromStore(
            emergencyID: UUID,
            userID: String,
            userLatitude: Double,
            userLongitude: Double,
        ): Emergency {
            return Emergency(
                emergencyID = EmergencyID(emergencyID),
                userID = UserID(userID),
                userLatitude = UserLatitude(userLatitude),
                userLongitude = UserLongitude(userLongitude),
            )
        }

        fun create(
            userID: String,
            userLatitude: Double,
            userLongitude: Double,
        ): Emergency {
            return Emergency(
                emergencyID = EmergencyID(UUID.randomUUID()),
                userID = UserID(userID),
                userLatitude = UserLatitude(userLatitude),
                userLongitude = UserLongitude(userLongitude),
            )
        }
    }
}

@JvmInline
value class EmergencyID(val value: UUID)

@JvmInline
value class UserLatitude(val value: Double)

@JvmInline
value class UserLongitude(val value: Double)
