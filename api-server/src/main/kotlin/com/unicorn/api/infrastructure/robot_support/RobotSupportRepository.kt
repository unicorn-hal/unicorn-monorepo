package com.unicorn.api.infrastructure.robot_support

import com.unicorn.api.domain.robot_support.*
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface RobotSupportRepository {
    fun store(robotSupport: RobotSupport): RobotSupport

    fun getOrNull(robotSupportID: RobotSupportID): RobotSupport?

    fun delete(robotSupport: RobotSupport)
}

@Repository
class RobotSupportRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : RobotSupportRepository {
    override fun store(robotSupport: RobotSupport): RobotSupport {
        val sql =
            """
            INSERT INTO robot_supports (
                robot_support_id,
                robot_id,
                emergency_queue_id,
                created_at
            ) VALUES (
                :robotSupportID,
                :robotID,
                :emergencyID,
                NOW()
            )
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("robotSupportID", robotSupport.robotSupportID.value)
                .addValue("robotID", robotSupport.robotID.value)
                .addValue("emergencyID", robotSupport.emergencyID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)

        return robotSupport
    }

    override fun getOrNull(robotSupportID: RobotSupportID): RobotSupport? {
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
                .addValue("robotSupportID", robotSupportID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            RobotSupport.fromStore(
                robotSupportID = UUID.fromString(rs.getString("robot_support_id")),
                robotID = rs.getString("robot_id"),
                emergencyID = UUID.fromString(rs.getString("emergency_queue_id")),
            )
        }.singleOrNull()
    }

    override fun delete(robotSupport: RobotSupport) {
        // language=postgresql
        val sql =
            """
            UPDATE robot_supports
            SET deleted_at = NOW()
            WHERE robot_support_id = :robotSupportID
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("robotSupportID", robotSupport.robotSupportID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
