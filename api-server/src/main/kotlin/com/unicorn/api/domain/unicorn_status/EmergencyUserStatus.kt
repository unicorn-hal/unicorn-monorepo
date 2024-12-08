package com.unicorn.api.domain.unicorn_status

import com.unicorn.api.domain.robot.RobotID
import com.unicorn.api.domain.robot.RobotName

data class EmergencyUserStatus private constructor(
    val status: Status,
    val waitingNumber: WaitingNumber?,
    val robotID: RobotID?,
    val robotName: RobotName?,
    val robotLatitude: RobotLatitude?,
    val robotLongitude: RobotLongitude?,
) {
    companion object {
        fun dispatch(
            robotID: String,
            robotName: String,
        ): EmergencyUserStatus {
            return EmergencyUserStatus(
                status = Status.dispatch,
                waitingNumber = null,
                robotID = RobotID(robotID),
                robotName = RobotName(robotName),
                robotLatitude = null,
                robotLongitude = null,
            )
        }

        fun waiting(waitingNumber: Int): EmergencyUserStatus {
            return EmergencyUserStatus(
                status = Status.user_waiting,
                waitingNumber = WaitingNumber(waitingNumber),
                robotID = null,
                robotName = null,
                robotLatitude = null,
                robotLongitude = null,
            )
        }

        fun arrival(
            robotID: String,
            robotName: String,
            robotLatitude: Double,
            robotLongitude: Double,
        ): EmergencyUserStatus {
            return EmergencyUserStatus(
                status = Status.arrival,
                waitingNumber = null,
                robotID = RobotID(robotID),
                robotName = RobotName(robotName),
                robotLatitude = RobotLatitude(robotLatitude),
                robotLongitude = RobotLongitude(robotLongitude),
            )
        }

        fun moving(
            robotID: String,
            robotName: String,
            robotLatitude: Double,
            robotLongitude: Double,
        ): EmergencyUserStatus {
            return EmergencyUserStatus(
                status = Status.moving,
                waitingNumber = null,
                robotID = RobotID(robotID),
                robotName = RobotName(robotName),
                robotLatitude = RobotLatitude(robotLatitude),
                robotLongitude = RobotLongitude(robotLongitude),
            )
        }

        fun complete(
            robotID: String,
            robotName: String,
        ): EmergencyUserStatus {
            return EmergencyUserStatus(
                status = Status.complete,
                waitingNumber = null,
                robotID = RobotID(robotID),
                robotName = RobotName(robotName),
                robotLatitude = null,
                robotLongitude = null,
            )
        }
    }
}

enum class Status {
    dispatch,
    user_waiting,
    arrival,
    moving,
    complete,
}

@JvmInline
value class WaitingNumber(val value: Int)

@JvmInline
value class RobotLatitude(val value: Double)

@JvmInline
value class RobotLongitude(val value: Double)
