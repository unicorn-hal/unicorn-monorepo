package com.unicorn.api.application_service.robot

import com.unicorn.api.domain.robot.RobotID
import com.unicorn.api.infrastructure.robot.RobotRepository
import org.springframework.stereotype.Service

interface DeleteRobotService {
    fun delete(robotID: RobotID): Unit
}

@Service
class DeleteRobotServiceImpl(
    private val robotRepository: RobotRepository,
) : DeleteRobotService {
    override fun delete(robotID: RobotID) {
        val robot = robotRepository.getOrNullBy(robotID)
        requireNotNull(robot) { "Robot not found" }

        robotRepository.delete(robot)
    }
}
