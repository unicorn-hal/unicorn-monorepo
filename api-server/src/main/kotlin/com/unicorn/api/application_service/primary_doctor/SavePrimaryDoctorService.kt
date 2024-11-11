package com.unicorn.api.application_service.primary_doctor

import com.unicorn.api.controller.primary_doctor.PrimaryDoctorRequest
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.primary_doctor.PrimaryDoctors
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.account.AccountRepository
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.primary_doctor.PrimaryDoctorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface SavePrimaryDoctorService {
    fun save(
        uid: String,
        primaryDoctorPostRequest: PrimaryDoctorRequest,
    ): PrimaryDoctors
}

@Service
class SavePrimaryDoctorServiceImpl(
    private val accountRepository: AccountRepository,
    private val doctorRepository: DoctorRepository,
    private val primaryDoctorRepository: PrimaryDoctorRepository,
) : SavePrimaryDoctorService {
    @Transactional
    override fun save(
        uid: String,
        primaryDoctorPostRequest: PrimaryDoctorRequest,
    ): PrimaryDoctors {
        val account = accountRepository.getOrNullByUid(UID(uid))
        requireNotNull(account) { "Account not found" }
        require(account.isUser()) { "Account is not user" }

        val doctorIDList =
            primaryDoctorPostRequest.doctorIDs.map { doctorID ->
                doctorRepository.getOrNullBy(DoctorID(doctorID))
                    ?: throw IllegalArgumentException("Doctor not found for ID: $doctorID")
                DoctorID(doctorID)
            }

        val primaryDoctors =
            PrimaryDoctors.create(
                userID = UserID(uid),
                doctorIDs = doctorIDList,
            )

        primaryDoctorRepository.store(primaryDoctors)

        return primaryDoctors
    }
}
