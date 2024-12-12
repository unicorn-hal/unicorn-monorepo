package com.unicorn.api.application_service.primary_doctor

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.primary_doctor.PrimaryDoctorRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface DeletePrimaryDoctorService {
    fun delete(
        userID: UserID,
        doctorID: DoctorID,
    ): Unit
}

@Service
class DeletePrimaryDoctorServiceImpl(
    private val doctorRepository: DoctorRepository,
    private val primaryDoctorRepository: PrimaryDoctorRepository,
    private val userRepository: UserRepository,
) : DeletePrimaryDoctorService {
    override fun delete(
        userID: UserID,
        doctorID: DoctorID,
    ) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val doctor = doctorRepository.getOrNullBy(doctorID)
        requireNotNull(doctor) { "Doctor not found" }

        val primaryDoctor = primaryDoctorRepository.getOrNullByDoctorIDAndUserID(doctorID, userID)
        requireNotNull(primaryDoctor) { "Primary doctor not found" }

        primaryDoctorRepository.delete(primaryDoctor)
    }
}
