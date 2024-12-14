package com.unicorn.api.application_service.emergency

import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.emergency.*
import com.unicorn.api.domain.robot.*
import com.unicorn.api.domain.robot_support.*
import com.unicorn.api.domain.unicorn_status.*
import com.unicorn.api.infrastructure.account.AccountRepository
import com.unicorn.api.infrastructure.emergency.EmergencyRepository
import com.unicorn.api.infrastructure.robot.RobotRepository
import com.unicorn.api.infrastructure.robot_support.RobotSupportRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.util.*

interface RobotRequestService {
    fun request(emergency: Emergency): Pair<EmergencyUserStatus?, EmergencyRobotStatus?>
}

@Service
class RobotRequestServiceImpl(
    private val emergencyRepository: EmergencyRepository,
    private val robotRepository: RobotRepository,
    private val robotSupportRepository: RobotSupportRepository,
    private val accountRepository: AccountRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : RobotRequestService {
    override fun request(emergency: Emergency): Pair<EmergencyUserStatus?, EmergencyRobotStatus?> {
        emergencyRepository.getOrNullBy(emergency.emergencyID) ?: return Pair(null, null)
        val robot = robotRepository.getWaitingOrNull()
        if (robot == null) {
            val waitingCount = emergencyRepository.getWaitingCount(emergency.emergencyID)
            if (waitingCount == null) return Pair(null, null)
            val emergencyUserStatus = EmergencyUserStatus.waiting(waitingCount)
            return Pair(emergencyUserStatus, null)
        }

        val robotSupport = RobotSupport.create(robot.robotID.value, emergency.emergencyID.value)
        robotSupportRepository.store(robotSupport)

        val emergencyUserStatus =
            EmergencyUserStatus.dispatch(
                robotID = robot.robotID.value,
                robotName = robot.robotName.value,
            )

        val uid = UID(emergency.userID.value)
        val account = accountRepository.getOrNullByUid(uid)
        if (account == null) return Pair(null, null)

        val emergencyRobotStatus =
            EmergencyRobotStatus.create(
                robotSupportID = robotSupport.robotSupportID.value,
                userID = emergency.userID.value,
                userLatitude = emergency.userLatitude.value,
                userLongitude = emergency.userLongitude.value,
                fcmTokenID = account.fcmTokenId.value,
            )

        val supportStatus = robot.updateStatus(RobotStatus.supporting)
        robotRepository.store(supportStatus)

        emergencyRepository.delete(emergency)

        applicationEventPublisher.publishEvent(RobotDispatchedEvent(emergency))

        return Pair(emergencyUserStatus, emergencyRobotStatus)
    }
}
