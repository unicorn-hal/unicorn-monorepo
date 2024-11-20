package com.unicorn.api.query_service.call

import com.fasterxml.jackson.annotation.JsonFormat
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

interface CallQueryService {
    fun get(
        doctorID: DoctorID,
        userID: UserID,
    ): CallResult?

    fun getByDoctorID(doctorID: DoctorID): CallResult?
}

@Service
class CallQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : CallQueryService {
    override fun get(
        doctorID: DoctorID,
        userID: UserID,
    ): CallResult? {
        //language=postgresql
        val sql =
            """
            SELECT
                call_reservation_id,
                doctor_id,
                user_id,
                call_start_time,
                call_end_time
            FROM call_reservations
            WHERE doctor_id = :doctor_id
            AND user_id = :user_id
            AND call_start_time >= CURRENT_TIMESTAMP
            AND deleted_at IS NULL
            ORDER BY call_start_time ASC
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("doctor_id", doctorID.value)
                .addValue("user_id", userID.value)
        val calls =
            namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                CallDto(
                    callReservationID = UUID.fromString(rs.getString("call_reservation_id")),
                    doctorID = rs.getString("doctor_id"),
                    userID = rs.getString("user_id"),
                    callStartTime = rs.getObject("call_start_time", OffsetDateTime::class.java),
                    callEndTime = rs.getObject("call_end_time", OffsetDateTime::class.java),
                )
            }
        if (calls.isEmpty()) {
            return null
        }
        return CallResult(calls)
    }

    override fun getByDoctorID(doctorID: DoctorID): CallResult? {
        //language=postgresql
        val sql =
            """
            SELECT
                call_reservation_id,
                doctor_id,
                user_id,
                call_start_time,
                call_end_time
            FROM call_reservations
            WHERE doctor_id = :doctor_id
            AND call_start_time >= CURRENT_TIMESTAMP
            AND call_start_time <= CURRENT_TIMESTAMP + INTERVAL '1 YEAR'
            AND deleted_at IS NULL
            ORDER BY call_start_time ASC
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("doctor_id", doctorID.value)
        val calls =
            namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                CallDto(
                    callReservationID = UUID.fromString(rs.getString("call_reservation_id")),
                    doctorID = rs.getString("doctor_id"),
                    userID = rs.getString("user_id"),
                    callStartTime = rs.getObject("call_start_time", OffsetDateTime::class.java),
                    callEndTime = rs.getObject("call_end_time", OffsetDateTime::class.java),
                )
            }
        if (calls.isEmpty()) {
            return null
        }
        return CallResult(calls)
    }
}

data class CallDto(
    val callReservationID: UUID,
    val doctorID: String,
    val userID: String,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    var callStartTime: OffsetDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    var callEndTime: OffsetDateTime,
) {
    init {
        val jstOffset = ZoneOffset.ofHours(9)
        callStartTime = callStartTime.withOffsetSameInstant(jstOffset)
        callEndTime = callEndTime.withOffsetSameInstant(jstOffset)
    }
}

data class CallResult(
    val data: List<CallDto>,
)
