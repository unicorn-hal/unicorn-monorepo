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
        assertEquals(RobotStatus.shutdown, robot.status)
    }

    @Test
    fun `should create robot from store`() {
        val robot =
            Robot.fromStore(
                robotID = "test",
                robotName = "robotName",
                status = "robot_waiting",
            )

        assertEquals("test", robot.robotID.value)
        assertEquals("robotName", robot.robotName.value)
        assertEquals(RobotStatus.robot_waiting, robot.status)
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
    fun `should update robot status supporting`() {
        val robot =
            Robot.create(
                robotID = "test",
                robotName = "robotName",
            )
        val newRobot = robot.updateStatus(RobotStatus.supporting)
        assertEquals(RobotStatus.supporting, newRobot.status)
    }

    @Test
    fun `should update robot status waiting`() {
        val robot =
            Robot.create(
                robotID = "test",
                robotName = "robotName",
            )
        val newRobot = robot.updateStatus(RobotStatus.robot_waiting)
        assertEquals(RobotStatus.robot_waiting, newRobot.status)
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
                    status = "badStatus",
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

    @Test
    fun `should power off robot`() {
        val robot =
            Robot.fromStore(
                robotID = "test",
                robotName = "robotName",
                status = "robot_waiting",
            )
        val newRobot = robot.power(RobotStatus.shutdown.toString())
        assertEquals(RobotStatus.shutdown, newRobot.status)
    }

    @Test
    fun `should power on robot`() {
        val robot =
            Robot.fromStore(
                robotID = "test",
                robotName = "robotName",
                status = "shutdown",
            )
        val newRobot = robot.power(RobotStatus.robot_waiting.toString())
        assertEquals(RobotStatus.robot_waiting, newRobot.status)
    }

    @Test
    fun `should return error when robot is supporting`() {
        val robot =
            Robot.fromStore(
                robotID = "test",
                robotName = "robotName",
                status = "supporting",
            )
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                robot.power(RobotStatus.shutdown.toString())
            }
        assertEquals("Robot is supporting", exception.message)
    }

    @Test
    fun `should return error when robot status is already shutdown`() {
        val robot =
            Robot.fromStore(
                robotID = "test",
                robotName = "robotName",
                status = "shutdown",
            )
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                robot.power(RobotStatus.shutdown.toString())
            }
        assertEquals("Robot status is already shutdown", exception.message)
    }

    @Test
    fun `should return error when robot status is already robot_waiting`() {
        val robot =
            Robot.fromStore(
                robotID = "test",
                robotName = "robotName",
                status = "robot_waiting",
            )
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                robot.power(RobotStatus.robot_waiting.toString())
            }
        assertEquals("Robot status is already robot_waiting", exception.message)
    }

    @Test
    fun `should return error when robot status is invalid`() {
        val robot =
            Robot.fromStore(
                robotID = "test",
                robotName = "robotName",
                status = "robot_waiting",
            )
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                robot.power("invalid")
            }
        assertEquals("robot status is invalid", exception.message)
    }
}
