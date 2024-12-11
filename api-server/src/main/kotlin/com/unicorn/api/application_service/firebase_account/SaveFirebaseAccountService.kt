package com.unicorn.api.application_service.firebase_account

import com.unicorn.api.config.FirebaseClient
import com.unicorn.api.controller.firebase_account.FirebaseAccountDto
import com.unicorn.api.controller.firebase_account.FirebaseAccountPostRequest
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import org.springframework.stereotype.Service

interface SaveFirebaseAccountService {
    fun save(
        uid: DoctorID,
        firebaseAccountPostRequest: FirebaseAccountPostRequest,
    ): FirebaseAccountDto
}

@Service
class SaveFirebaseAccountServiceImpl(
    val firebaseClient: FirebaseClient,
    val doctorRepository: DoctorRepository,
) : SaveFirebaseAccountService {
    override fun save(
        uid: DoctorID,
        firebaseAccountPostRequest: FirebaseAccountPostRequest,
    ): FirebaseAccountDto {
        val doctor = doctorRepository.getOrNullBy(uid)
        requireNotNull(doctor) { "Doctor not found" }

        val firebaseAccountUid = firebaseClient.createAccount(firebaseAccountPostRequest.email, firebaseAccountPostRequest.password)
        return FirebaseAccountDto(
            email = firebaseAccountPostRequest.email,
            password = firebaseAccountPostRequest.password,
            uid = firebaseAccountUid,
        )
    }
}
