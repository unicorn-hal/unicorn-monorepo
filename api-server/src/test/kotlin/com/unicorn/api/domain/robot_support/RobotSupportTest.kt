package com.unicorn.api.domain.robot_support

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class RobotSupportTest {
    @Test
    fun `should create robotSupport`() {
        val robotSupport =
            RobotSupport.create(
                "robotID",
                UUID.fromString("1b0991d0-6138-034a-40f1-aa5ed887ff72"),
            )
        assertEquals("robotID", robotSupport.robotID.value)
        assertEquals(UUID.fromString("1b0991d0-6138-034a-40f1-aa5ed887ff72"), robotSupport.emergencyID.value)
    }

    @Test
    fun `should create robotSupport from store`() {
        val robotSupport =
            RobotSupport.fromStore(
                UUID.fromString("c64e788c-dd0a-72d8-a271-5460e1f29816"),
                "robotID",
                UUID.fromString("1b0991d0-6138-034a-40f1-aa5ed887ff72"),
            )
        assertEquals(UUID.fromString("c64e788c-dd0a-72d8-a271-5460e1f29816"), robotSupport.robotSupportID.value)
        assertEquals("robotID", robotSupport.robotID.value)
        assertEquals(UUID.fromString("1b0991d0-6138-034a-40f1-aa5ed887ff72"), robotSupport.emergencyID.value)
    }
}
