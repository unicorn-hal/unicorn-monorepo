package com.unicorn.api.application_service.primary_doctor

import com.unicorn.api.domain.primary_doctor.PrimaryDoctorID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.primary_doctor.PrimaryDoctorRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface DeletePrimaryDoctorService {
    fun delete(
        userID: UserID,
        primaryDoctorID: PrimaryDoctorID,
    ): Unit
}

@Service
class DeletePrimaryDoctorServiceImpl(
    private val primaryDoctorRepository: PrimaryDoctorRepository,
    private val userRepository: UserRepository,
) : DeletePrimaryDoctorService {
    override fun delete(
        userID: UserID,
        primaryDoctorID: PrimaryDoctorID,
    ) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }
        val primaryDoctor = primaryDoctorRepository.getOrNullBy(primaryDoctorID)
        requireNotNull(primaryDoctor) { "Primary doctor not found" }
        primaryDoctorRepository.delete(primaryDoctor)
    }
}
