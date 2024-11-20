package com.unicorn.api.domain.call

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.util.UUID

class CallTest {
    val callStartTime = OffsetDateTime.now()
    val callEndTime = OffsetDateTime.now().plusDays(1)

    @Test
    fun `should create callReservation`() {
        val call =
            Call.create(
                doctorID = "12345",
                userID = "12345",
                callStartTime = callStartTime,
                callEndTime = callEndTime,
            )

        assertEquals("12345", call.doctorID.value)
        assertEquals("12345", call.userID.value)
        assertEquals(callStartTime, call.callStartTime.value)
        assertEquals(callEndTime, call.callEndTime.value)
    }

    @Test
    fun `should not create callReservation with invalid time`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Call.create(
                    doctorID = "12345",
                    userID = "12345",
                    callStartTime = callEndTime,
                    callEndTime = callStartTime,
                )
            }
        assertEquals("The call start time must be before the end time.", exception.message)
    }

    @Test
    fun `should create callReservation from store`() {
        val call =
            Call.fromStore(
                callReservationID = UUID.randomUUID(),
                doctorID = "12345",
                userID = "12345",
                callStartTime = callStartTime,
                callEndTime = callEndTime,
            )

        assertEquals("12345", call.doctorID.value)
        assertEquals("12345", call.userID.value)
        assertEquals(callStartTime, call.callStartTime.value)
        assertEquals(callEndTime, call.callEndTime.value)
    }

    @Test
    fun `should update callReservation`() {
        val call =
            Call.create(
                doctorID = "12345",
                userID = "12345",
                callStartTime = callStartTime,
                callEndTime = callEndTime,
            )

        val updatedCall =
            call.update(
                doctorID = DoctorID("67890"),
                userID = UserID("67890"),
                callStartTime = callStartTime.plusDays(1),
                callEndTime = callEndTime.plusDays(1),
            )

        assertEquals("67890", updatedCall.doctorID.value)
        assertEquals("67890", updatedCall.userID.value)
        assertEquals(callStartTime.plusDays(1), updatedCall.callStartTime.value)
        assertEquals(callEndTime.plusDays(1), updatedCall.callEndTime.value)
    }
}
