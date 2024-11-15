package com.unicorn.api.domain.call

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
        val callStartTime = OffsetDateTime.now().plusDays(1)
        val callEndTime = OffsetDateTime.now()
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Call.create(
                    doctorID = "12345",
                    userID = "12345",
                    callStartTime = callStartTime,
                    callEndTime = callEndTime,
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
}
