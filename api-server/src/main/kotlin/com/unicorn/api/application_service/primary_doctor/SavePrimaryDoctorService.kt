package com.unicorn.api.application_service.primary_doctor

import com.unicorn.api.controller.primary_doctor.PrimaryDoctorRequest
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.primary_doctor.PrimaryDoctor
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.primary_doctor.PrimaryDoctorRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import java.util.*

interface SavePrimaryDoctorService {
    fun save(
        userID: UserID,
        primaryDoctorRequest: PrimaryDoctorRequest,
    ): PrimaryDoctor
}

@Service
class SavePrimaryDoctorServiceImpl(
    private val primaryDoctorRepository: PrimaryDoctorRepository,
    private val userRepository: UserRepository,
    private val doctorRepository: DoctorRepository,
) : SavePrimaryDoctorService {
    override fun save(
        userID: UserID,
        primaryDoctorRequest: PrimaryDoctorRequest,
    ): PrimaryDoctor {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val doctorID = DoctorID(primaryDoctorRequest.doctorID)
        val doctor = doctorRepository.getOrNullBy(doctorID)
        requireNotNull(doctor) { "Doctor not found" }

        val primaryDoctor =
            PrimaryDoctor.create(
                userID = userID.value,
                doctorID = doctorID.value,
            )
        primaryDoctorRepository.store(primaryDoctor)

        return primaryDoctor
    }
}
