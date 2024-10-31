package com.unicorn.api.domain.call_support

import com.unicorn.api.domain.doctor.DoctorID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalTime
import java.util.*

class CallSupportTest {
    @Test
    fun `create call support`() {
        val doctorID = DoctorID("doctorID")
        val callSupportStartHour = LocalTime.of(9, 0)
        val callSupportEndHour = LocalTime.of(17, 0)

        val callSupport =
            CallSupport.create(
                doctorID,
                callSupportStartHour,
                callSupportEndHour,
            )

        assertEquals(doctorID, callSupport.doctorID)
        assertEquals(callSupportStartHour, callSupport.callSupportStartHour.value)
        assertEquals(callSupportEndHour, callSupport.callSupportEndHour.value)
    }

    @Test
    fun `should create call support from store`() {
        val callSupportID = UUID.randomUUID()
        val doctorID = "doctorID"
        val callSupportStartHour = LocalTime.of(9, 0)
        val callSupportEndHour = LocalTime.of(17, 0)

        val callSupport =
            CallSupport.fromStore(
                callSupportID,
                doctorID,
                callSupportStartHour,
                callSupportEndHour,
            )

        assertEquals(callSupportID, callSupport.callSupportID.value)
        assertEquals(doctorID, callSupport.doctorID.value)
        assertEquals(callSupportStartHour, callSupport.callSupportStartHour.value)
        assertEquals(callSupportEndHour, callSupport.callSupportEndHour.value)
    }

    @Test
    fun `should update call support`() {
        val doctorID = DoctorID("doctorID")
        val callSupportStartHour = LocalTime.of(9, 0)
        val callSupportEndHour = LocalTime.of(17, 0)
        val callSupport =
            CallSupport.fromStore(
                UUID.randomUUID(),
                doctorID.value,
                callSupportStartHour,
                callSupportEndHour,
            )
        val newCallSupportStartHour = LocalTime.of(10, 0)
        val newCallSupportEndHour = LocalTime.of(18, 0)

        val updatedCallSupport =
            callSupport.update(
                CallSupportStartHour(newCallSupportStartHour),
                CallSupportEndHour(newCallSupportEndHour),
            )

        assertEquals(doctorID, updatedCallSupport.doctorID)
        assertEquals(newCallSupportStartHour, updatedCallSupport.callSupportStartHour.value)
        assertEquals(newCallSupportEndHour, updatedCallSupport.callSupportEndHour.value)
    }

    @Test
    fun `should throw exception when creating call support with start hour after end hour`() {
        val doctorID = DoctorID("doctorID")
        val callSupportStartHour = LocalTime.of(9, 0)
        val callSupportEndHour = LocalTime.of(17, 0)

        assertThrows(IllegalArgumentException::class.java) {
            CallSupport.create(
                doctorID,
                callSupportEndHour,
                callSupportStartHour,
            )
        }
    }

    @Test
    fun `should throw exception when updating call support with start hour after end hour`() {
        val doctorID = DoctorID("doctorID")
        val callSupportStartHour = LocalTime.of(9, 0)
        val callSupportEndHour = LocalTime.of(17, 0)

        val callSupport =
            CallSupport.create(
                doctorID,
                callSupportStartHour,
                callSupportEndHour,
            )

        val newCallSupportStartHour = LocalTime.of(18, 0)
        val newCallSupportEndHour = LocalTime.of(17, 0)

        assertThrows(IllegalArgumentException::class.java) {
            callSupport.update(
                CallSupportStartHour(newCallSupportStartHour),
                CallSupportEndHour(newCallSupportEndHour),
            )
        }
    }

    @Test
    fun `should throw exception when creating call support with start hour equal to end hour`() {
        val doctorID = DoctorID("doctorID")
        val callSupportStartHour = LocalTime.of(17, 0)
        val callSupportEndHour = LocalTime.of(17, 0)

        assertThrows(IllegalArgumentException::class.java) {
            CallSupport.create(
                doctorID,
                callSupportStartHour,
                callSupportEndHour,
            )
        }
    }

    @Test
    fun `should throw exception when updating call support with start hour equal to end hour`() {
        val doctorID = DoctorID("doctorID")
        val callSupportStartHour = LocalTime.of(9, 0)
        val callSupportEndHour = LocalTime.of(17, 0)

        val callSupport =
            CallSupport.create(
                doctorID,
                callSupportStartHour,
                callSupportEndHour,
            )

        val newCallSupportStartHour = LocalTime.of(17, 0)
        val newCallSupportEndHour = LocalTime.of(17, 0)

        assertThrows(IllegalArgumentException::class.java) {
            callSupport.update(
                CallSupportStartHour(newCallSupportStartHour),
                CallSupportEndHour(newCallSupportEndHour),
            )
        }
    }
}
