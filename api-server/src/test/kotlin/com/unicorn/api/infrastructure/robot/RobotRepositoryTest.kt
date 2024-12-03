package com.unicorn.api.infrastructure.robot

import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.robot.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/robot/Insert_Parent_Account_Data.sql")
@Sql("/db/robot/Insert_Robot_Data.sql")
class RobotRepositoryTest {
    @Autowired
    private lateinit var robotRepository: RobotRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findRobotBy(robotID: String): Robot? {
        // language=postgresql
        val sql =
            """
            SELECT
                robot_id,
                name,
                status
            FROM robots
            WHERE robot_id = :robotID
            AND deleted_at IS NULL
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("robotID", robotID)
        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            Robot.fromStore(
                robotID = rs.getString("robot_id"),
                robotName = rs.getString("name"),
                robotStatus = rs.getString("status"),
            )
        }.singleOrNull()
    }

    @Test
    fun `should store robot`() {
        val robot =
            Robot.create(
                robotID = "robot",
                robotName = "robotName",
            )
        robotRepository.store(robot)

        val foundRobot = findRobotBy(robot.robotID.value)
        assertNotNull(foundRobot!!)
        assertEquals("robot", foundRobot.robotID.value)
        assertEquals("robotName", foundRobot.robotName.value)
        assertEquals(RobotStatus.robot_waiting, foundRobot.robotStatus)
    }

    @Test
    fun `should update robot name`() {
        val robot =
            Robot.create(
                robotID = "robot",
                robotName = "robotName",
            )
        robotRepository.store(robot)

        val newRobot = robot.updateName(RobotName("newRobotName"))
        robotRepository.store(newRobot)

        val foundRobot = findRobotBy(robot.robotID.value)
        assertNotNull(foundRobot!!)
        assertEquals("robot", foundRobot.robotID.value)
        assertEquals("newRobotName", foundRobot.robotName.value)
        assertEquals(RobotStatus.robot_waiting, foundRobot.robotStatus)
    }

    @Test
    fun `should change supporting robot status`() {
        val robot =
            Robot.create(
                robotID = "robot",
                robotName = "robotName",
            )
        robotRepository.store(robot)

        val newRobot = robot.updateStatus(RobotStatus.supporting)
        robotRepository.store(newRobot)

        val foundRobot = findRobotBy(robot.robotID.value)
        assertNotNull(foundRobot!!)
        assertEquals("robot", foundRobot.robotID.value)
        assertEquals("robotName", foundRobot.robotName.value)
        assertEquals(RobotStatus.supporting, foundRobot.robotStatus)
    }

    @Test
    fun `should find robot by robotID`() {
        val robot = UID("test")
        val foundRobot = robotRepository.getOrNullBy(robot)
        assertNotNull(foundRobot!!)
        assertEquals("test", foundRobot.robotID.value)
        assertEquals("robotName", foundRobot.robotName.value)
        assertEquals(RobotStatus.robot_waiting, foundRobot.robotStatus)
    }

    @Test
    fun `should find waiting robot`() {
        val robot = robotRepository.getWaitingOrNull()
        assertNotNull(robot!!)
        assertEquals("test", robot.robotID.value)
        assertEquals("robotName", robot.robotName.value)
        assertEquals(RobotStatus.robot_waiting, robot.robotStatus)
    }

    @Test
    fun `should delete robot`() {
        val robot = UID("test")
        val foundRobot = robotRepository.getOrNullBy(robot)
        robotRepository.delete(foundRobot!!)
        val deletedRobot = findRobotBy(robot.value)
        assertNull(deletedRobot)
    }

    @Test
    fun `should not find deleted robot`() {
        val robot = UID("12345")

        val foundRobot = robotRepository.getOrNullBy(robot)
        assertNull(foundRobot)
    }

    @Test
    fun `should not find null robot`() {
        val robot = UID("null")

        val foundRobot = robotRepository.getOrNullBy(robot)
        assertNull(foundRobot)
    }
}
