package com.unicorn.api.domain.call_support

import com.unicorn.api.domain.doctor.DoctorID
import java.time.LocalTime
import java.util.*

data class CallSupport private constructor(
    val callSupportID: CallSupportID,
    val doctorID: DoctorID,
    val callSupportStartHour: CallSupportStartHour,
    val callSupportEndHour: CallSupportEndHour,
) {
    companion object {
        fun fromStore(
            callSupportID: UUID,
            doctorID: String,
            callSupportStartHour: LocalTime,
            callSupportEndHour: LocalTime,
        ): CallSupport {
            return CallSupport(
                CallSupportID(callSupportID),
                DoctorID(doctorID),
                CallSupportStartHour(callSupportStartHour),
                CallSupportEndHour(callSupportEndHour),
            )
        }

        fun create(
            doctorID: DoctorID,
            callSupportStartHour: LocalTime,
            callSupportEndHour: LocalTime,
        ): CallSupport {
            require(callSupportStartHour.isBefore(callSupportEndHour)) {
                "Start hour must be before end hour"
            }

            return CallSupport(
                CallSupportID(UUID.randomUUID()),
                doctorID,
                CallSupportStartHour(callSupportStartHour),
                CallSupportEndHour(callSupportEndHour),
            )
        }
    }

    fun update(
        callSupportStartHour: CallSupportStartHour,
        callSupportEndHour: CallSupportEndHour,
    ): CallSupport {
        require(callSupportStartHour.value.isBefore(callSupportEndHour.value)) {
            "Start hour must be before end hour"
        }

        return this.copy(
            callSupportStartHour = callSupportStartHour,
            callSupportEndHour = callSupportEndHour,
        )
    }
}

@JvmInline
value class CallSupportID(val value: UUID)

@JvmInline
value class CallSupportStartHour(val value: LocalTime)

@JvmInline
value class CallSupportEndHour(val value: LocalTime)
