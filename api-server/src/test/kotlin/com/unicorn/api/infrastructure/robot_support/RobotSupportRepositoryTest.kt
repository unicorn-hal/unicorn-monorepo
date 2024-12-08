package com.unicorn.api.infrastructure.robot_support

import com.unicorn.api.domain.robot_support.*
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
@Sql("/db/robot_support/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/robot_support/Insert_Robot_Data.sql")
@Sql("/db/emergency/Insert_Emergency_Data.sql")
@Sql("/db/robot_support/Insert_Robot_Support_Data.sql")
class RobotSupportRepositoryTest {
    @Autowired
    private lateinit var robotSupportRepository: RobotSupportRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findRobotSupportBy(robotSupportID: UUID): RobotSupport? {
        // language=postgresql
        val sql =
            """
            SELECT
                robot_support_id,
                robot_id,
                emergency_queue_id
            FROM robot_supports
            WHERE robot_support_id = :robotSupportID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("robotSupportID", robotSupportID)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            RobotSupport.fromStore(
                robotSupportID = UUID.fromString(rs.getString("robot_support_id")),
                robotID = rs.getString("robot_id"),
                emergencyID = UUID.fromString(rs.getString("emergency_queue_id")),
            )
        }.singleOrNull()
    }

    @Test
    fun `should store robotSupport`() {
        val robotSupport =
            RobotSupport.create(
                "robot",
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d469"),
            )

        robotSupportRepository.store(robotSupport)

        val storedRobotSupport = findRobotSupportBy(robotSupport.robotSupportID.value)
        assertNotNull(storedRobotSupport!!)
        assertEquals(robotSupport.robotID, storedRobotSupport.robotID)
        assertEquals(robotSupport.emergencyID, storedRobotSupport.emergencyID)
    }

    @Test
    fun `should find robotSupport`() {
        val robotSupportID = RobotSupportID(UUID.fromString("c64e788c-dd0a-72d8-a271-5460e1f29816"))
        val robotSupport = robotSupportRepository.getOrNull(robotSupportID)
        assertNotNull(robotSupport)
        assertEquals("robot", robotSupport!!.robotID.value)
        assertEquals(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470"), robotSupport.emergencyID.value)
    }

    @Test
    fun `should not find deleted robotSupport`() {
        val robotSupportID = RobotSupportID(UUID.fromString("c64e788c-dd0a-72d8-a271-5460e1f29817"))
        val robotSupport = robotSupportRepository.getOrNull(robotSupportID)
        assertNull(robotSupport)
    }

    @Test
    fun `should not find robotSupport`() {
        val robotSupportID = RobotSupportID(UUID.fromString("c64e788c-dd0a-72d8-a271-5460e1f29818"))
        val robotSupport = robotSupportRepository.getOrNull(robotSupportID)
        assertNull(robotSupport)
    }

    @Test
    fun `should delete robotSupport`() {
        val robotSupport =
            RobotSupport.create(
                "robot",
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d469"),
            )
        robotSupportRepository.store(robotSupport)
        robotSupportRepository.delete(robotSupport)
        val deletedRobotSupport = findRobotSupportBy(robotSupport.robotSupportID.value)
        assertNull(deletedRobotSupport)
    }
}
