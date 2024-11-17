package com.unicorn.api.application_service.call

import com.unicorn.api.controller.call.CallPostRequest
import com.unicorn.api.domain.call.Call
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.call.CallRepository
import com.unicorn.api.infrastructure.call_support.CallSupportRepository
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import java.time.ZoneOffset

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

        val jstOffset = ZoneOffset.ofHours(9) // JSTのオフセット（UTC+9）

        // callStartTime と callEndTime を JST (UTC+9) で LocalTime に変換
        val callStartTimeJst = callPostRequest.callStartTime.withOffsetSameInstant(jstOffset).toLocalTime()
        val callEndTimeJst = callPostRequest.callEndTime.withOffsetSameInstant(jstOffset).toLocalTime()

        // doctorCallSupport のサポート開始時間と終了時間も LocalTime として取得
        val supportStartTimeJst = doctorCallSupport.callSupportStartHour.value
        val supportEndTimeJst = doctorCallSupport.callSupportEndHour.value

        // サポート時間内であることを確認
        require(
            supportStartTimeJst <= callStartTimeJst && callEndTimeJst <= supportEndTimeJst,
        ) {
            "The call start and end times do not match the doctor's support hours"
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
