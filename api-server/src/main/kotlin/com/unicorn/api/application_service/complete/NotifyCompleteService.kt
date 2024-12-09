package com.unicorn.api.application_service.complete

import com.unicorn.api.controller.complete.CompletePostRequest
import com.unicorn.api.domain.emergency.EmergencySavedEvent
import com.unicorn.api.domain.robot.RobotID
import com.unicorn.api.domain.robot.RobotStatus
import com.unicorn.api.domain.robot_support.RobotSupportID
import com.unicorn.api.domain.unicorn_status.EmergencyUserStatus
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.emergency.EmergencyRepository
import com.unicorn.api.infrastructure.robot.RobotRepository
import com.unicorn.api.infrastructure.robot_support.RobotSupportRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.util.*

interface NotifyCompleteService {
    fun notify(
        robotID: RobotID,
        completePostRequest: CompletePostRequest,
    ): EmergencyUserStatus
}

@Service
class NotifyCompleteServiceImpl(
    private val robotRepository: RobotRepository,
    private val emergencyRepository: EmergencyRepository,
    private val robotSupportRepository: RobotSupportRepository,
    private val userRepository: UserRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : NotifyCompleteService {
    override fun notify(
        robotID: RobotID,
        completePostRequest: CompletePostRequest,
    ): EmergencyUserStatus {
        val userID = UserID(completePostRequest.userID)
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val robot = robotRepository.getOrNullBy(robotID)
        requireNotNull(robot) { "Robot not found" }

        val robotSupportID = RobotSupportID(completePostRequest.robotSupportID)
        val robotSupport = robotSupportRepository.getOrNull(robotSupportID)
        requireNotNull(robotSupport) { "Robot support not found" }

        robotSupportRepository.delete(robotSupport)

        val waitingRobot = robot.updateStatus(RobotStatus.robot_waiting)
        robotRepository.store(waitingRobot)

        val emergencyUserStatus =
            EmergencyUserStatus.complete(
                robotID = robotID.value,
                robotName = robot.robotName.value,
            )

        val waitingEmergencyList = emergencyRepository.getOlderOrNull()
        for (waitingEmergency in waitingEmergencyList) {
            applicationEventPublisher.publishEvent(EmergencySavedEvent(waitingEmergency))
        }
        return emergencyUserStatus
    }
}
