package com.unicorn.api.application_service.call

import com.unicorn.api.controller.call.CallPostRequest
import com.unicorn.api.domain.call.Call
import com.unicorn.api.domain.call.service.CallDomainService
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.call.CallRepository
import com.unicorn.api.infrastructure.call_support.CallSupportRepository
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface SaveCallService {
    fun save(
        userID: UserID,
        callPostRequest: CallPostRequest,
    ): Call
}

@Service
class SaveCallServiceImpl(
    private val userRepository: UserRepository,
    private val doctorRepository: DoctorRepository,
    private val callSupportRepository: CallSupportRepository,
    private val callRepository: CallRepository,
    private val callDomainService: CallDomainService,
) : SaveCallService {
    override fun save(
        userID: UserID,
        callPostRequest: CallPostRequest,
    ): Call {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val doctor = doctorRepository.getOrNullBy(DoctorID(callPostRequest.doctorID))
        requireNotNull(doctor) { "Doctor not found" }

        val doctorCallSupport = callSupportRepository.getOrNullBy(DoctorID(callPostRequest.doctorID))
        requireNotNull(doctorCallSupport) { "Call Support not found" }

        // CallTimeServiceで時間のチェックを実行
        callDomainService.validateCallTimes(callPostRequest, doctorCallSupport)

        // 予約時間が重複していないかをチェック
        require(!callRepository.isOverlapping(callPostRequest.callStartTime, callPostRequest.callEndTime, callPostRequest.doctorID)) {
            "The requested call time overlaps with an existing reservation for this doctor."
        }

        val call =
            Call.create(
                userID = userID.value,
                doctorID = callPostRequest.doctorID,
                callStartTime = callPostRequest.callStartTime,
                callEndTime = callPostRequest.callEndTime,
            )

        callRepository.store(call)

        return call
    }
}
