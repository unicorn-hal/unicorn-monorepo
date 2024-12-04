package com.unicorn.api.domain.robot

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class RobotTest {
    @Test
    fun `should create robot`() {
        val robot =
            Robot.create(
                robotID = "test",
                robotName = "robotName",
            )
        assertEquals("test", robot.robotID.value)
        assertEquals("robotName", robot.robotName.value)
        assertEquals(RobotStatus.robot_waiting, robot.robotStatus)
    }

    @Test
    fun `should create robot from store`() {
        val robot =
            Robot.fromStore(
                robotID = "test",
                robotName = "robotName",
                robotStatus = "robot_waiting",
            )

        assertEquals("test", robot.robotID.value)
        assertEquals("robotName", robot.robotName.value)
        assertEquals(RobotStatus.robot_waiting, robot.robotStatus)
    }

    @Test
    fun `should update robot name`() {
        val robot =
            Robot.create(
                robotID = "test",
                robotName = "robotName",
            )
        val newRobot = robot.updateName(RobotName("newRobotName"))
        assertEquals("newRobotName", newRobot.robotName.value)
    }

    @Test
    fun `should update robot status`() {
        val robot =
            Robot.create(
                robotID = "test",
                robotName = "robotName",
            )
        val newRobot = robot.updateStatus(RobotStatus.supporting)
        assertEquals(RobotStatus.supporting, newRobot.robotStatus)
    }

    @Test
    fun `should return error message when null robot name`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Robot.create(
                    robotID = "test",
                    robotName = "",
                )
            }

        assertEquals("Robot name should not be blank", exception.message)
    }

    @Test
    fun `should return error when bad status`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Robot.fromStore(
                    robotID = "test",
                    robotName = "robotName",
                    robotStatus = "badStatus",
                )
            }
        assertEquals("robot status is invalid", exception.message)
    }

    @Test
    fun `should return error when robotID is blank`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Robot.create(
                    robotID = "",
                    robotName = "robotName",
                )
            }
        assertEquals("Robot ID should not be blank", exception.message)
    }
}
