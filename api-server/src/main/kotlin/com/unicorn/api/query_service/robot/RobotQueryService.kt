package com.unicorn.api.query_service.robot

import com.unicorn.api.domain.robot.RobotID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

interface RobotQueryService {
    fun getAll(): RobotResult

    fun getOrNullBy(robotID: RobotID): RobotDto?
}

@Service
class RobotQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : RobotQueryService {
    override fun getAll(): RobotResult {
        // language=postgresql
        val sql =
            """
            SELECT 
                robot_id,
                name
            from robots
            WHERE deleted_at IS NULL
            """.trimIndent()
        val result =
            namedParameterJdbcTemplate.query(
                sql,
            ) { rs, _ ->
                RobotDto(
                    robotID = rs.getString("robot_id"),
                    robotName = rs.getString("name"),
                )
            }
        return RobotResult(result)
    }

    override fun getOrNullBy(robotID: RobotID): RobotDto? {
        // language=postgresql
        val sql =
            """
            SELECT 
                robot_id,
                name
            from robots
            WHERE robot_id = :robotID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams = MapSqlParameterSource().addValue("robotID", robotID.value)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams,
        ) { rs, _ ->
            RobotDto(
                robotID = rs.getString("robot_id"),
                robotName = rs.getString("name"),
            )
        }.singleOrNull()
    }
}

data class RobotResult(
    val data: List<RobotDto>,
)

data class RobotDto(
    val robotID: String,
    val robotName: String,
)
