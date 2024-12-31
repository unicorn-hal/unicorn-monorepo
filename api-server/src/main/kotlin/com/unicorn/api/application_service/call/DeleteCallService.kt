package com.unicorn.api.application_service.call

import com.unicorn.api.domain.call.CallReservationID
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.call.CallRepository
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface DeleteCallService {
    fun delete(
        callReservationID: CallReservationID,
        userID: UserID,
    )

    fun deleteByUserID(userID: UserID)

    fun deleteByDoctorID(doctorID: DoctorID)
}

@Service
class DeleteCallServiceImpl(
    private val userRepository: UserRepository,
    private val doctorRepository: DoctorRepository,
    private val callRepository: CallRepository,
) : DeleteCallService {
    override fun delete(
        callReservationID: CallReservationID,
        userID: UserID,
    ) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val callReservation = callRepository.getOrNullBy(callReservationID)
        requireNotNull(callReservation) { "Call reservation not found" }

        callRepository.delete(callReservation)
    }

    override fun deleteByUserID(userID: UserID) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        callRepository.deleteByUserID(userID)
    }

    override fun deleteByDoctorID(doctorID: DoctorID) {
        val doctor = doctorRepository.getOrNullBy(doctorID)
        requireNotNull(doctor) { "Doctor not found" }

        callRepository.deleteByDoctor(doctor)
    }
}
