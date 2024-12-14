package com.unicorn.api.domain.unicorn_status

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class EmergencyUserStatusTest {
    @Test
    fun `should dispatch status`() {
        val emergencyUserStatus =
            EmergencyUserStatus.dispatch(
                robotID = "robotID",
                robotName = "robotName",
            )

        assertNotNull(emergencyUserStatus)
        assertEquals(Status.dispatch, emergencyUserStatus.status)
        assertNull(emergencyUserStatus.waitingNumber)
        assertEquals("robotID", emergencyUserStatus.robotID?.value)
        assertEquals("robotName", emergencyUserStatus.robotName?.value)
        assertNull(emergencyUserStatus.robotLatitude?.value)
        assertNull(emergencyUserStatus.robotLongitude?.value)
    }

    @Test
    fun `should waiting status`() {
        val emergencyUserStatus = EmergencyUserStatus.waiting(1)

        assertNotNull(emergencyUserStatus)
        assertEquals(Status.user_waiting, emergencyUserStatus.status)
        assertEquals(1, emergencyUserStatus.waitingNumber?.value)
        assertNull(emergencyUserStatus.robotID)
        assertNull(emergencyUserStatus.robotName)
        assertNull(emergencyUserStatus.robotLatitude)
        assertNull(emergencyUserStatus.robotLongitude)
    }

    @Test
    fun `should arrival status`() {
        val emergencyUserStatus =
            EmergencyUserStatus.arrival(
                robotID = "robotID",
                robotName = "robotName",
                robotLatitude = 1.0,
                robotLongitude = 2.0,
            )

        assertNotNull(emergencyUserStatus)
        assertEquals(Status.arrival, emergencyUserStatus.status)
        assertNull(emergencyUserStatus.waitingNumber)
        assertEquals("robotID", emergencyUserStatus.robotID?.value)
        assertEquals("robotName", emergencyUserStatus.robotName?.value)
        assertEquals(1.0, emergencyUserStatus.robotLatitude?.value)
        assertEquals(2.0, emergencyUserStatus.robotLongitude?.value)
    }

    @Test
    fun `should moving status`() {
        val emergencyUserStatus =
            EmergencyUserStatus.moving(
                robotID = "robotID",
                robotName = "robotName",
                robotLatitude = 1.0,
                robotLongitude = 2.0,
            )

        assertNotNull(emergencyUserStatus)
        assertEquals(Status.moving, emergencyUserStatus.status)
        assertNull(emergencyUserStatus.waitingNumber)
        assertEquals("robotID", emergencyUserStatus.robotID?.value)
        assertEquals("robotName", emergencyUserStatus.robotName?.value)
        assertEquals(1.0, emergencyUserStatus.robotLatitude?.value)
        assertEquals(2.0, emergencyUserStatus.robotLongitude?.value)
    }

    @Test
    fun `should complete status`() {
        val emergencyUserStatus =
            EmergencyUserStatus.complete(
                robotID = "robotID",
                robotName = "robotName",
            )

        assertNotNull(emergencyUserStatus)
        assertEquals(Status.complete, emergencyUserStatus.status)
        assertNull(emergencyUserStatus.waitingNumber)
        assertEquals("robotID", emergencyUserStatus.robotID?.value)
        assertEquals("robotName", emergencyUserStatus.robotName?.value)
        assertNull(emergencyUserStatus.robotLatitude?.value)
        assertNull(emergencyUserStatus.robotLongitude?.value)
    }

    @Test
    fun `should all shutdown status`() {
        val emergencyUserStatus = EmergencyUserStatus.allShutdown()

        assertNotNull(emergencyUserStatus)
        assertEquals(Status.all_shutdown, emergencyUserStatus.status)
        assertNull(emergencyUserStatus.waitingNumber)
        assertNull(emergencyUserStatus.robotID)
        assertNull(emergencyUserStatus.robotName)
        assertNull(emergencyUserStatus.robotLatitude)
        assertNull(emergencyUserStatus.robotLongitude)
    }
}
