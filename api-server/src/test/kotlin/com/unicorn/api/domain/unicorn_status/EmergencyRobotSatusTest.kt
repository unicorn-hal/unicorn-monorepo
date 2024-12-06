package com.unicorn.api.domain.unicorn_status

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class EmergencyRobotStatusTest {
    @Test
    fun `should create emergency robot status`() {
        val emergencyRobotStatus =
            EmergencyRobotStatus.create(
                robotSupportID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d471"),
                userID = "userID",
                userLatitude = 1.0,
                userLongitude = 1.0,
                fcmTokenID = "fcmTokenID",
            )

        assertEquals(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d471"), emergencyRobotStatus.robotSupportID.value)
        assertEquals("userID", emergencyRobotStatus.userID.value)
        assertEquals(1.0, emergencyRobotStatus.userLatitude.value)
        assertEquals(1.0, emergencyRobotStatus.userLongitude.value)
        assertEquals("fcmTokenID", emergencyRobotStatus.fcmTokenID.value)
    }
}
