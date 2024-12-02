package com.unicorn.api.application_service.emergency

import com.unicorn.api.domain.emergency.EmergencySavedEvent
import com.unicorn.api.domain.web_socket.UserWebSocket
import com.unicorn.api.infrastructure.emergency.EmergencyRepository
import org.springframework.stereotype.Service
// import com.unicorn.api.domain.robot.*
// import com.unicorn.api.infrastructure.robot.RobotRepository
// import com.unicorn.api.infrastructure.RobotSupportRepository

interface RobotRequestService {
    fun request(emergencySavedEvent: EmergencySavedEvent): UserWebSocket
}

@Service
class RobotRequestServiceImpl(
    private val emergencyRepository: EmergencyRepository,
//     // private val robotRepository: RobotRepository,
//     // private val robotSupportRepository: RobotSupportRepository,
) : RobotRequestService {
    override fun request(emergencySavedEvent: EmergencySavedEvent): UserWebSocket {
        // val robot = robotRepository.getWaitingRobot()
        //     if (robot != null) {
        //         robotSupportRepository.create(robot.robotID, emergencySavedEvent.emergency.emergencyID)
        //         emergencyRepository.delete(emergencySavedEvent.emergency)
        //         return UserWebSocket.active(
        //             // robotID = robot.robotID.value,
        //             // robotName = robot.robotName.value,
        //         )
        //     } else {
        return UserWebSocket.waiting(
            emergencyRepository.getWaitingCount(emergencySavedEvent.emergency.emergencyID),
        )
        //     }
    }
}
