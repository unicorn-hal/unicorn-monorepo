package com.unicorn.api.application_service.robot

import com.unicorn.api.controller.robot.RobotPowerRequest
import com.unicorn.api.domain.robot.Robot
import com.unicorn.api.domain.robot.RobotID
import com.unicorn.api.infrastructure.robot.RobotRepository
import org.springframework.stereotype.Service

interface UpdateRobotStatusService {
    fun power(
        robotID: RobotID,
        robotPowerRequest: RobotPowerRequest,
    ): Robot
}

@Service
class UpdateRobotStatusServiceImpl(
    val robotRepository: RobotRepository,
) : UpdateRobotStatusService {
    override fun power(
        robotID: RobotID,
        robotPowerRequest: RobotPowerRequest,
    ): Robot {
        val robot = robotRepository.getOrNullBy(robotID)
        requireNotNull(robot) { "Robot not found" }

        val newRobot = robot.power(robotPowerRequest.status)

        robotRepository.store(newRobot)

        return newRobot
    }
}
