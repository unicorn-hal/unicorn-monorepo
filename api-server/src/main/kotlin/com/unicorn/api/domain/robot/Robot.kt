package com.unicorn.api.domain.robot

import com.unicorn.api.domain.account.UID

data class Robot private constructor(
    val robotID: UID,
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
                robotID = UID(robotID),
                robotName = RobotName(robotName),
                robotStatus = RobotStatus.robot_waiting,
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
                robotID = UID(robotID),
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
}

enum class RobotStatus {
    supporting,
    robot_waiting,
}

@JvmInline
value class RobotName(val value: String) {
    init {
        require(value.isNotBlank()) {
            "Robot name should not be blank"
        }
    }
}
