package com.unicorn.api.domain.robot_support

import com.unicorn.api.domain.emergency.EmergencyID
import com.unicorn.api.domain.robot.RobotID
import java.util.*

data class RobotSupport private constructor(
    val robotSupportID: RobotSupportID,
    val robotID: RobotID,
    val emergencyID: EmergencyID,
) {
    companion object {
        fun fromStore(
            robotSupportID: UUID,
            robotID: String,
            emergencyID: UUID,
        ): RobotSupport {
            return RobotSupport(
                robotSupportID = RobotSupportID(robotSupportID),
                robotID = RobotID(robotID),
                emergencyID = EmergencyID(emergencyID),
            )
        }

        fun create(
            robotID: String,
            emergencyID: UUID,
        ): RobotSupport {
            return RobotSupport(
                robotSupportID = RobotSupportID(UUID.randomUUID()),
                robotID = RobotID(robotID),
                emergencyID = EmergencyID(emergencyID),
            )
        }
    }
}

@JvmInline
value class RobotSupportID(val value: UUID)
