package com.unicorn.api.domain.call

import java.time.OffsetDateTime
import java.util.UUID

data class Call
    private constructor(
        val callReservationID: CallReservationID,
        val doctorID: DoctorID,
        val userID: UserID,
        val callStartTime: CallStartTime,
        val callEndTime: CallEndTime,
    ) {
        companion object {
            fun fromStore(
                callReservationID: UUID,
                doctorID: String,
                userID: String,
                callStartTime: OffsetDateTime,
                callEndTime: OffsetDateTime,
            ): Call {
                return Call(
                    callReservationID = CallReservationID(callReservationID),
                    doctorID = DoctorID(doctorID),
                    userID = UserID(userID),
                    callStartTime = CallStartTime(callStartTime),
                    callEndTime = CallEndTime(callEndTime),
                )
            }

            fun create(
                callReservationID: UUID,
                doctorID: String,
                userID: String,
                callStartTime: OffsetDateTime,
                callEndTime: OffsetDateTime,
            ): Call {
                val startTime = CallStartTime(callStartTime)
                val endTime = CallEndTime(callEndTime)

                require(startTime.value.isBefore(endTime.value)) {
                    "The call start time must be before the end time."
                }

                return Call(
                    callReservationID = CallReservationID(UUID.randomUUID()),
                    doctorID = DoctorID(doctorID),
                    userID = UserID(userID),
                    callStartTime = startTime,
                    callEndTime = endTime,
                )
            }
        }

        fun update(
            callReservationID: CallReservationID,
            doctorID: DoctorID,
            userID: UserID,
            callStartTime: CallStartTime,
            callEndTime: CallEndTime,
        ): Call {
            return this.copy(
                callReservationID = callReservationID,
                doctorID = doctorID,
                userID = userID,
                callStartTime = callStartTime,
                callEndTime = callEndTime,
            )
        }
    }

@JvmInline
value class CallReservationID(val value: UUID)

@JvmInline
value class DoctorID(val value: String) {
    init {
        require(value.isNotBlank()) { "doctorID should not be blank" }
    }
}

@JvmInline
value class UserID(val value: String) {
    init {
        require(value.isNotBlank()) { "userID should not be blank" }
    }
}

@JvmInline
value class CallStartTime(val value: OffsetDateTime)

@JvmInline
value class CallEndTime(val value: OffsetDateTime)
