package com.unicorn.api.application_service.robot

import com.unicorn.api.controller.robot.SaveRobotRequest
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.robot.Robot
import com.unicorn.api.domain.robot.RobotID
import com.unicorn.api.infrastructure.account.AccountRepository
import com.unicorn.api.infrastructure.robot.RobotRepository
import org.springframework.stereotype.Service

interface SaveRobotService {
    fun save(
        uid: UID,
        saveRobotRequest: SaveRobotRequest,
    ): SaveRobotRequest
}

@Service
class SaveRobotServiceImpl(
    private val accountRepository: AccountRepository,
    private val robotRepository: RobotRepository,
) : SaveRobotService {
    override fun save(
        uid: UID,
        saveRobotRequest: SaveRobotRequest,
    ): SaveRobotRequest {
        val account = accountRepository.getOrNullByUid(uid)
        requireNotNull(account) { "Account not found" }
        require(account.isRobot()) { "Account is not robot" }

        val existingRobot = robotRepository.getOrNullBy(RobotID(account.uid.value))
        require(existingRobot == null) { "Robot already exists" }

        val robot =
            Robot.create(
                robotID = account.uid.value,
                saveRobotRequest.robotName,
            )

        robotRepository.store(robot)
        return saveRobotRequest
    }
}
