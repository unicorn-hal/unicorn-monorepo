package com.unicorn.api.domain.robot

data class Robot private constructor(
    val robotID: RobotID,
    val robotName: RobotName,
    val status: RobotStatus,
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
                status = RobotStatus.shutdown,
            )
        }

        fun fromStore(
            robotID: String,
            robotName: String,
            status: String,
        ): Robot {
            if (status !in RobotStatus.entries.map { it.name }) {
                throw InvalidRoleException()
            }
            return Robot(
                robotID = RobotID(robotID),
                robotName = RobotName(robotName),
                status = RobotStatus.valueOf(status),
            )
        }
    }

    fun updateName(robotName: RobotName): Robot {
        return this.copy(
            robotName = robotName,
        )
    }

    fun updateStatus(status: RobotStatus): Robot {
        return this.copy(
            status = status,
        )
    }

    fun power(status: String): Robot {
        if (status !in RobotStatus.entries.map { it.name }) {
            throw InvalidRoleException()
        }
        val newStatus = RobotStatus.valueOf(status)
        require(this.status != RobotStatus.supporting) { "Robot is supporting" }
        require(this.status != newStatus) { "Robot status is already $status" }

        return this.copy(
            status = newStatus,
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
