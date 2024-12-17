package com.unicorn.api.application_service.firebase_account

import com.unicorn.api.config.FirebaseClient
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.robot.RobotID
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.robot.RobotRepository
import org.springframework.stereotype.Service

interface DeleteFirebaseAccountService {
    fun delete(
        doctorID: DoctorID,
        robotID: RobotID,
    ): Unit
}

@Service
class DeleteFirebaseAccountServiceImpl(
    private val doctorRepository: DoctorRepository,
    private val robotRepository: RobotRepository,
    private val firebaseClient: FirebaseClient,
) : DeleteFirebaseAccountService {
    override fun delete(
        doctorID: DoctorID,
        robotID: RobotID,
    ) {
        val doctor = doctorRepository.getOrNullBy(doctorID)
        requireNotNull(doctor) { "Doctor not found" }

        val robot = robotRepository.getOrNullBy(robotID)
        requireNotNull(robot) { "Robot not found" }

        firebaseClient.deleteAccount(robotID.value)
    }
}
