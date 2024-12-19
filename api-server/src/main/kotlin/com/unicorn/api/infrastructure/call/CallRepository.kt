package com.unicorn.api.infrastructure.call

import com.unicorn.api.domain.call.Call
import com.unicorn.api.domain.call.CallReservationID
import com.unicorn.api.domain.doctor.Doctor
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.util.toJST
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

interface CallRepository {
    fun store(call: Call): Call

    fun getOrNullBy(callReservationID: CallReservationID): Call?

    fun delete(call: Call): Unit

    fun isOverlapping(
        callStartTime: OffsetDateTime,
        callEndTime: OffsetDateTime,
        doctorID: String,
    ): Boolean

    fun deleteByUserID(userID: UserID)

    fun deleteByDoctor(doctor: Doctor)
}

@Repository
class CallRepositoryImpl(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : CallRepository {
    override fun store(call: Call): Call {
        // language=postgresql
        val sql =
            """
            INSERT INTO call_reservations (
                call_reservation_id,
                doctor_id,
                user_id,
                call_start_time,
                call_end_time,
                created_at
            ) VALUES (
                :callReservationID,
                :doctorID,
                :userID,
                :callStartTime,
                :callEndTime,
                NOW()
            )
            ON CONFLICT (call_reservation_id)
            DO UPDATE SET
                doctor_id = EXCLUDED.doctor_id,
                user_id = EXCLUDED.user_id,
                call_start_time = EXCLUDED.call_start_time,
                call_end_time = EXCLUDED.call_end_time,
                deleted_at = NULL
            WHERE call_reservations.created_at IS NOT NULL;
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("callReservationID", call.callReservationID.value)
                .addValue("doctorID", call.doctorID.value)
                .addValue("userID", call.userID.value)
                .addValue("callStartTime", call.callStartTime.value)
                .addValue("callEndTime", call.callEndTime.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
        return call
    }

    override fun getOrNullBy(callReservationID: CallReservationID): Call? {
        // language=postgresql
        val sql =
            """
            SELECT
                call_reservation_id,
                doctor_id,
                user_id,
                call_start_time,
                call_end_time
            FROM call_reservations
            WHERE call_reservation_id = :callReservationID
                AND deleted_at IS NULL
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("callReservationID", callReservationID.value)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams,
        ) { rs, _ ->
            Call.fromStore(
                callReservationID = UUID.fromString(rs.getString("call_reservation_id")),
                doctorID = rs.getString("doctor_id"),
                userID = rs.getString("user_id"),
                callStartTime = rs.getObject("call_start_time", OffsetDateTime::class.java).toJST(),
                callEndTime = rs.getObject("call_end_time", OffsetDateTime::class.java).toJST(),
            )
        }.singleOrNull()
    }

    override fun delete(call: Call) {
        // language=postgresql
        val sql =
            """
            UPDATE call_reservations
            SET deleted_at = NOW()
            WHERE call_reservation_id = :callReservationID
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("callReservationID", call.callReservationID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }

    override fun isOverlapping(
        callStartTime: OffsetDateTime,
        callEndTime: OffsetDateTime,
        doctorID: String,
    ): Boolean {
        // language=postgresql
        val sql =
            """
            SELECT COUNT(*) 
            FROM call_reservations
            WHERE doctor_id = :doctorID
            AND deleted_at IS NULL
            AND (
                call_start_time < :callEndTime
                AND call_end_time > :callStartTime
            )
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("callStartTime", callStartTime)
                .addValue("callEndTime", callEndTime)
                .addValue("doctorID", doctorID)

        val count = namedParameterJdbcTemplate.queryForObject(sql, sqlParams, Int::class.java)
        return count != null && count > 0
    }

    override fun deleteByUserID(userID: UserID) {
        val sql =
            """
            UPDATE call_reservations
            SET deleted_at = NOW()
            WHERE user_id = :userID
            AND deleted_at IS NULL;
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", userID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }

    override fun deleteByDoctor(doctor: Doctor) {
        val sql =
            """
            UPDATE call_reservations
            SET deleted_at = NOW()
            WHERE doctor_id = :doctorID
            AND deleted_at IS NULL;
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("doctorID", doctor.doctorID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
