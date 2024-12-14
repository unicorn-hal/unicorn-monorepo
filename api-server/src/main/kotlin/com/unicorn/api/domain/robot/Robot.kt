package com.unicorn.api.domain.robot

data class Robot private constructor(
    val robotID: RobotID,
    val robotName: RobotName,
    val robotStatus: RobotStatus,
) {
    class InvalidRoleException : IllegalArgumentException("robot status is invalid")

    companion object {
        fun create(
            robotID: String,
            robotName: String,
        ): Robot {
            return Robot(
                robotID = RobotID(robotID),
                robotName = RobotName(robotName),
                robotStatus = RobotStatus.shutdown,
            )
        }

        fun fromStore(
            robotID: String,
            robotName: String,
            robotStatus: String,
        ): Robot {
            if (robotStatus !in RobotStatus.entries.map { it.name }) {
                throw InvalidRoleException()
            }
            return Robot(
                robotID = RobotID(robotID),
                robotName = RobotName(robotName),
                robotStatus = RobotStatus.valueOf(robotStatus),
            )
        }
    }

    fun updateName(robotName: RobotName): Robot {
        return this.copy(
            robotName = robotName,
        )
    }

    fun updateStatus(robotStatus: RobotStatus): Robot {
        return this.copy(
            robotStatus = robotStatus,
        )
    }

    fun power(robotStatus: RobotStatus): Robot {
        require(this.robotStatus != RobotStatus.supporting) { "Robot is supporting" }
        require(this.robotStatus != robotStatus) { "Robot status is already $robotStatus" }

        return this.copy(
            robotStatus = robotStatus,
        )
    }
}

enum class RobotStatus {
    supporting,
    robot_waiting,
    shutdown,
}

@JvmInline
value class RobotID(val value: String) {
    init {
        require(value.isNotBlank()) { "Robot ID should not be blank" }
    }
}

@JvmInline
value class RobotName(val value: String) {
    init {
        require(value.isNotBlank()) {
            "Robot name should not be blank"
        }
    }
}
