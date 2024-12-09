package com.unicorn.api.application_service.moving

import com.unicorn.api.controller.moving.MovingPostRequest
import com.unicorn.api.domain.robot.RobotID
import com.unicorn.api.domain.unicorn_status.EmergencyUserStatus
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.robot.RobotRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import java.util.*

interface NotifyMovingService {
    fun notify(
        robotID: RobotID,
        movingPostRequest: MovingPostRequest,
    ): EmergencyUserStatus
}

@Service
class NotifyMovingServiceImpl(
    private val robotRepository: RobotRepository,
    private val userRepository: UserRepository,
) : NotifyMovingService {
    override fun notify(
        robotID: RobotID,
        movingPostRequest: MovingPostRequest,
    ): EmergencyUserStatus {
        val userID = UserID(movingPostRequest.userID)
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val robot = robotRepository.getOrNullBy(robotID)
        requireNotNull(robot) { "Robot not found" }

        val emergencyUserStatus =
            EmergencyUserStatus.moving(
                robotID = robotID.value,
                robotName = robot.robotName.value,
                robotLatitude = movingPostRequest.robotLatitude,
                robotLongitude = movingPostRequest.robotLongitude,
            )

        return emergencyUserStatus
    }
}
