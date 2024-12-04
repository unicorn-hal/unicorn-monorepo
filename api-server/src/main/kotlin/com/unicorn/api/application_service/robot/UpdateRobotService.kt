package com.unicorn.api.application_service.robot

import com.unicorn.api.controller.robot.UpdateRobotRequest
import com.unicorn.api.domain.robot.RobotID
import com.unicorn.api.domain.robot.RobotName
import com.unicorn.api.infrastructure.robot.RobotRepository
import org.springframework.stereotype.Service

interface UpdateRobotService {
    fun update(
        robotID: RobotID,
        updateRobotRequest: UpdateRobotRequest,
    ): UpdateRobotRequest
}

@Service
class UpdateRobotServiceImpl(
    private val robotRepository: RobotRepository,
) : UpdateRobotService {
    override fun update(
        robotID: RobotID,
        updateRobotRequest: UpdateRobotRequest,
    ): UpdateRobotRequest {
        val robot = robotRepository.getOrNullBy(robotID)
        requireNotNull(robot) { "Robot not found" }

        val updatedRobot = robot.updateName(RobotName(updateRobotRequest.robotName))

        robotRepository.store(updatedRobot)
        return updateRobotRequest
    }
}
