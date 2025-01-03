package com.unicorn.api.domain.call.service

import com.unicorn.api.controller.call.CallPostRequest
import com.unicorn.api.domain.call_support.CallSupport
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Service
class CallDomainService {
    private val jstOffset = ZoneOffset.ofHours(9)

    fun validateCallTimes(
        callPostRequest: CallPostRequest,
        doctorCallSupport: CallSupport,
    ) {
        // 現在時刻を JST (UTC+9) で OffsetDateTime 型で取得
        val currentJstTime = OffsetDateTime.now(jstOffset)

        // callStartTime が現在時刻より前でないことを確認
        require(!callPostRequest.callStartTime.isBefore(currentJstTime)) {
            "The call start time cannot be in the past"
        }

        // callStartTime と callEndTime を JST (UTC+9) で LocalTime に変換
        val callStartTimeJst = callPostRequest.callStartTime.withOffsetSameInstant(jstOffset).toLocalTime()
        val callEndTimeJst = callPostRequest.callEndTime.withOffsetSameInstant(jstOffset).toLocalTime()

        // doctorCallSupport のサポート開始時間と終了時間も LocalTime として取得
        val supportStartTimeJst = doctorCallSupport.callSupportStartHour.value
        val supportEndTimeJst = doctorCallSupport.callSupportEndHour.value

        // サポート時間内であることを確認
        require(!(supportStartTimeJst > callStartTimeJst || callEndTimeJst > supportEndTimeJst)) {
            "The call start and end times do not match the doctor's support hours"
        }
    }
}
