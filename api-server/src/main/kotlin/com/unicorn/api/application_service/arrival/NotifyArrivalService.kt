package com.unicorn.api.application_service.arrival

import com.unicorn.api.controller.arrival.ArrivalPostRequest
import com.unicorn.api.domain.robot.RobotID
import com.unicorn.api.domain.unicorn_status.EmergencyUserStatus
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.robot.RobotRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import java.util.*

interface NotifyArrivalService {
    fun notify(
        robotID: RobotID,
        arrivalPostRequest: ArrivalPostRequest,
    ): EmergencyUserStatus
}

@Service
class NotifyArrivalServiceImpl(
    private val robotRepository: RobotRepository,
    private val userRepository: UserRepository,
) : NotifyArrivalService {
    override fun notify(
        robotID: RobotID,
        arrivalPostRequest: ArrivalPostRequest,
    ): EmergencyUserStatus {
        val userID = UserID(arrivalPostRequest.userID)
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val robot = robotRepository.getOrNullBy(robotID)
        requireNotNull(robot) { "Robot not found" }

        val emergencyUserStatus =
            EmergencyUserStatus.arrival(
                robotID = robotID.value,
                robotName = robot.robotName.value,
                robotLatitude = arrivalPostRequest.robotLatitude,
                robotLongitude = arrivalPostRequest.robotLongitude,
            )

        return emergencyUserStatus
    }
}
