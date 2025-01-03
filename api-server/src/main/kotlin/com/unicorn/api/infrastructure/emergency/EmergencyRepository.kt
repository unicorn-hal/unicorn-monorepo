package com.unicorn.api.infrastructure.emergency

import com.unicorn.api.domain.emergency.*
import com.unicorn.api.domain.user.User
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface EmergencyRepository {
    fun store(emergency: Emergency): Emergency

    fun getOrNullBy(emergencyID: EmergencyID): Emergency?

    fun getOlderOrNull(): List<Emergency>

    fun getWaitingCount(emergencyID: EmergencyID): Int?

    fun delete(emergency: Emergency)

    fun deleteByUser(user: User)
}

@Repository
class EmergencyRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : EmergencyRepository {
    override fun store(emergency: Emergency): Emergency {
        // language=postgresql
        val sql =
            """
            INSERT INTO emergency_queue (
                emergency_queue_id,
                user_id,
                user_latitude,
                user_longitude,
                created_at
            ) VALUES (
                :emergencyID,
                :userID,
                :userLatitude,
                :userLongitude,
                NOW()
            )
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("emergencyID", emergency.emergencyID.value)
                .addValue("userID", emergency.userID.value)
                .addValue("userLatitude", emergency.userLatitude.value)
                .addValue("userLongitude", emergency.userLongitude.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)

        return emergency
    }

    override fun getOrNullBy(emergencyID: EmergencyID): Emergency? {
        // language=postgresql
        val sql =
            """
            SELECT 
                emergency_queue_id,
                user_id,
                user_latitude,
                user_longitude
            FROM emergency_queue
            WHERE emergency_queue_id = :emergencyID
            AND deleted_at IS NULL

            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("emergencyID", emergencyID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            Emergency.fromStore(
                emergencyID = UUID.fromString(rs.getString("emergency_queue_id")),
                userID = rs.getString("user_id"),
                userLatitude = rs.getDouble("user_latitude"),
                userLongitude = rs.getDouble("user_longitude"),
            )
        }.singleOrNull()
    }

    override fun getOlderOrNull(): List<Emergency> {
        // language=postgresql
        val sql =
            """
            SELECT 
                emergency_queue_id,
                user_id,
                user_latitude,
                user_longitude
            FROM emergency_queue
            WHERE deleted_at IS NULL
            ORDER BY created_at ASC
            """.trimIndent()

        return namedParameterJdbcTemplate.query(sql) { rs, _ ->
            Emergency.fromStore(
                emergencyID = UUID.fromString(rs.getString("emergency_queue_id")),
                userID = rs.getString("user_id"),
                userLatitude = rs.getDouble("user_latitude"),
                userLongitude = rs.getDouble("user_longitude"),
            )
        }
    }

    override fun getWaitingCount(emergencyID: EmergencyID): Int? {
        // language=postgresql
        val sql =
            """
            SELECT row_number
            FROM (
                SELECT
                    ROW_NUMBER() OVER (
                        ORDER BY created_at ASC
                    ) AS row_number,
                    emergency_queue_id
                    FROM emergency_queue
                    WHERE deleted_at IS NULL
                ) AS subquery
            WHERE emergency_queue_id = :emergencyID
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("emergencyID", emergencyID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams, { rs, _ ->
            rs.getInt("row_number")
        }).singleOrNull()
    }

    override fun delete(emergency: Emergency) {
        // language=postgresql
        val sql =
            """
            UPDATE emergency_queue
            SET deleted_at = NOW()
            WHERE emergency_queue_id = :emergencyID
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("emergencyID", emergency.emergencyID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }

    override fun deleteByUser(user: User) {
        // language=postgresql
        val sql =
            """
            UPDATE emergency_queue
            SET deleted_at = NOW()
            WHERE user_id = :userID
            AND deleted_at IS NULL;
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", user.userID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
