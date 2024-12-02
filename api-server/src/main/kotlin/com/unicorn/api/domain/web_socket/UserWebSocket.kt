package com.unicorn.api.domain.web_socket

// import com.unicorn.api.domain.robot.RobotID
// import com.unicorn.api.domain.robot.RobotName

data class UserWebSocket private constructor(
    val status: Status,
    val waitingNumber: WaitingNumber?,
    // val robotID: RobotID?,
    // val robotName: RobotName?,
    val robotLatitude: RobotLatitude?,
    val robotLongitude: RobotLongitude?,
) {
    companion object {
        fun active(
            // robotID: String,
            // robotName: String,
        ): UserWebSocket {
            return UserWebSocket(
                status = Status.active,
                waitingNumber = null,
                // robotID = RobotID(robotID),
                // robotName = RobotName(robotName),
                robotLatitude = null,
                robotLongitude = null,
            )
        }

        fun waiting(waitingNumber: Int): UserWebSocket {
            return UserWebSocket(
                status = Status.waiting,
                waitingNumber = WaitingNumber(waitingNumber),
                // robotID = null,
                // robotName = null,
                robotLatitude = null,
                robotLongitude = null,
            )
        }
    }

    fun arrival(
        robotLatitude: RobotLatitude,
        robotLongitude: RobotLongitude,
    ): UserWebSocket {
        return this.copy(
            status = Status.arrival,
            robotLatitude = robotLatitude,
            robotLongitude = robotLongitude,
        )
    }

    fun moving(
        robotLatitude: RobotLatitude,
        robotLongitude: RobotLongitude,
    ): UserWebSocket {
        return this.copy(
            status = Status.moving,
            robotLatitude = robotLatitude,
            robotLongitude = robotLongitude,
        )
    }

    fun complete(): UserWebSocket {
        return this.copy(
            status = Status.complete,
        )
    }
}

enum class Status {
    active,
    waiting,
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
