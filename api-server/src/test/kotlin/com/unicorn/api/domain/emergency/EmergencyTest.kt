package com.unicorn.api.domain.emergency

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID

class EmergencyTest {
    @Test
    fun `should create emergency`() {
        val emergency =
            Emergency.create(
                userID = "test",
                userLatitude = 0.0,
                userLongitude = 0.0,
            )
        assertEquals("test", emergency.userID.value)
        assertEquals(0.0, emergency.userLatitude.value)
        assertEquals(0.0, emergency.userLongitude.value)
    }

    @Test
    fun `should create emergency from store`() {
        val emergency =
            Emergency.fromStore(
                emergencyID = UUID.randomUUID(),
                userID = "test",
                userLatitude = 0.0,
                userLongitude = 0.0,
            )
        assertEquals("test", emergency.userID.value)
        assertEquals(0.0, emergency.userLatitude.value)
        assertEquals(0.0, emergency.userLongitude.value)
    }
}
