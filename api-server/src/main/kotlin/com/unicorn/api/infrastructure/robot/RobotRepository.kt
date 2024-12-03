package com.unicorn.api.infrastructure.robot

import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.robot.Robot
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface RobotRepository {
    fun store(robot: Robot): Robot

    fun getOrNullBy(robotID: UID): Robot?

    fun getWaitingOrNull(): Robot?

    fun delete(robot: Robot)
}

@Repository
class RobotRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : RobotRepository {
    override fun store(robot: Robot): Robot {
        // language=postgresql
        val sql =
            """
            INSERT INTO robots (
                robot_id,
                name,
                status,
                created_at
            ) VALUES (
                :robotID,
                :robotName,
                :status::robot_status,
                NOW()
            )
            ON CONFLICT (robot_id)
            DO UPDATE SET
                name = EXCLUDED.name,
                status = EXCLUDED.status,
                deleted_at = NULL
            WHERE robots.created_at IS NOT NULL;
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("robotID", robot.robotID.value)
                .addValue("robotName", robot.robotName.value)
                .addValue("status", robot.robotStatus.toString())

        namedParameterJdbcTemplate.update(sql, sqlParams)
        return robot
    }

    override fun getOrNullBy(robotID: UID): Robot? {
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
                .addValue("robotID", robotID.value)
        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            Robot.fromStore(
                robotID = rs.getString("robot_id"),
                robotName = rs.getString("name"),
                robotStatus = rs.getString("status"),
            )
        }.singleOrNull()
    }

    override fun getWaitingOrNull(): Robot? {
        // language=postgresql
        val sql =
            """
            SELECT
                robot_id,
                name,
                status
            FROM robots
            WHERE status = 'robot_waiting'::robot_status
            AND deleted_at IS NULL
            LIMIT 1
            """.trimIndent()
        return namedParameterJdbcTemplate.query(sql) { rs, _ ->
            Robot.fromStore(
                robotID = rs.getString("robot_id"),
                robotName = rs.getString("name"),
                robotStatus = rs.getString("status"),
            )
        }.singleOrNull()
    }

    override fun delete(robot: Robot) {
        // language=postgresql
        val sql =
            """
            UPDATE robots
            SET deleted_at = NOW()
            WHERE robot_id = :robotID
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("robotID", robot.robotID.value)
        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
