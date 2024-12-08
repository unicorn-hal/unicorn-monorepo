package com.unicorn.api.domain.unicorn_status

import com.unicorn.api.domain.account.FCMTokenId
import com.unicorn.api.domain.emergency.UserLatitude
import com.unicorn.api.domain.emergency.UserLongitude
import com.unicorn.api.domain.robot_support.RobotSupportID
import com.unicorn.api.domain.user.UserID
import java.util.*

data class EmergencyRobotStatus private constructor(
    val robotSupportID: RobotSupportID,
    val userID: UserID,
    val userLatitude: UserLatitude,
    val userLongitude: UserLongitude,
    val fcmTokenID: FCMTokenId,
) {
    companion object {
        fun create(
            robotSupportID: UUID,
            userID: String,
            userLatitude: Double,
            userLongitude: Double,
            fcmTokenID: String,
        ): EmergencyRobotStatus {
            return EmergencyRobotStatus(
                robotSupportID = RobotSupportID(robotSupportID),
                userID = UserID(userID),
                userLatitude = UserLatitude(userLatitude),
                userLongitude = UserLongitude(userLongitude),
                fcmTokenID = FCMTokenId(fcmTokenID),
            )
        }
    }
}
