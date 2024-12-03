package com.unicorn.api.application_service.robot

import com.unicorn.api.controller.robot.SaveRobotRequest
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.robot.Robot
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
    val accountRepository: AccountRepository,
    val robotRepository: RobotRepository,
) : SaveRobotService {
    override fun save(
        uid: UID,
        saveRobotRequest: SaveRobotRequest,
    ): SaveRobotRequest {
        val account = accountRepository.getOrNullByUid(uid)
        requireNotNull(account) { "Account not found" }
        require(account.isRobot()) { "Account is not robot" }

        val robot =
            Robot.create(
                robotID = account.uid.value,
                saveRobotRequest.robotName,
            )

        robotRepository.store(robot)
        return saveRobotRequest
    }
}
