package com.unicorn.api.application_service.call

import com.unicorn.api.controller.call.CallPostRequest
import com.unicorn.api.domain.call.Call
import com.unicorn.api.domain.call.CallReservationID
import com.unicorn.api.domain.call.service.CallDomainService
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.call.CallRepository
import com.unicorn.api.infrastructure.call_support.CallSupportRepository
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

interface UpdateCallService {
    fun update(
        userID: UserID,
        callPutRequest: CallPostRequest,
        callReservationID: UUID,
    ): Call
}

@Service
class UpdateCallServiceImpl(
    private val userRepository: UserRepository,
    private val doctorRepository: DoctorRepository,
    private val callSupportRepository: CallSupportRepository,
    private val callRepository: CallRepository,
    private val callDomainService: CallDomainService,
) : UpdateCallService {
    override fun update(
        userID: UserID,
        callPutRequest: CallPostRequest,
        callReservationID: UUID,
    ): Call {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val doctor = doctorRepository.getOrNullBy(DoctorID(callPutRequest.doctorID))
        requireNotNull(doctor) { "Doctor not found" }

        val doctorCallSupport = callSupportRepository.getOrNullBy(DoctorID(callPutRequest.doctorID))
        requireNotNull(doctorCallSupport) { "Call Support not found" }

        val callsReservation = callRepository.getOrNullBy(CallReservationID(callReservationID))
        requireNotNull(callsReservation) { "Call Reservation not found" }

        // CallTimeServiceで時間のチェックを実行
        callDomainService.validateCallTimes(callPutRequest, doctorCallSupport)

        // 予約時間が重複していないかをチェック
        require(!callRepository.isOverlapping(callPutRequest.callStartTime, callPutRequest.callEndTime, callPutRequest.doctorID)) {
            "The requested call time overlaps with an existing reservation for this doctor."
        }

        val call =
            Call.fromStore(
                callReservationID = callReservationID,
                userID = userID.value,
                doctorID = callPutRequest.doctorID,
                callStartTime = callPutRequest.callStartTime,
                callEndTime = callPutRequest.callEndTime,
            )

        callRepository.store(call)

        return call
    }
}
