package com.unicorn.api.infrastructure.call_support

import com.unicorn.api.domain.call_support.CallSupport
import com.unicorn.api.domain.doctor.DoctorID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface CallSupportRepository {
    fun store(callSupport: CallSupport): CallSupport

    fun getOrNullBy(doctorID: DoctorID): CallSupport?

    fun delete(callSupport: CallSupport)
}

@Repository
class CallSupportRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : CallSupportRepository {
    override fun getOrNullBy(doctorID: DoctorID): CallSupport? {
        // language=postgresql
        val sql =
            """
            SELECT
                call_support_id, 
                doctor_id, 
                start_time, 
                end_time, 
                created_at
            FROM call_support_hours
            WHERE doctor_id = :doctorID
                AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("doctorID", doctorID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            CallSupport.fromStore(
                callSupportID = UUID.fromString(rs.getString("call_support_id")),
                doctorID = rs.getString("doctor_id"),
                callSupportStartHour = rs.getTime("start_time").toLocalTime(),
                callSupportEndHour = rs.getTime("end_time").toLocalTime(),
            )
        }.singleOrNull()
    }

    override fun store(callSupport: CallSupport): CallSupport {
        // language=postgresql
        val sql =
            """
            INSERT INTO call_support_hours (
                call_support_id, 
                doctor_id, 
                start_time, 
                end_time,
                created_at
            ) VALUES (
                :callSupportID, 
                :doctorID, 
                :callSupportStartHour, 
                :callSupportEndHour,
                NOW()
            )
            ON CONFLICT (call_support_id) DO UPDATE
            SET 
                doctor_id = EXCLUDED.doctor_id,
                start_time = EXCLUDED.start_time,
                end_time = EXCLUDED.end_time,
                deleted_at = NULL
            WHERE call_support_hours.created_at IS NOT NULL;
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("callSupportID", callSupport.callSupportID.value)
                .addValue("doctorID", callSupport.doctorID.value)
                .addValue("callSupportStartHour", callSupport.callSupportStartHour.value)
                .addValue("callSupportEndHour", callSupport.callSupportEndHour.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)

        return callSupport
    }

    override fun delete(callSupport: CallSupport) {
        // language=postgresql
        val sql =
            """
            UPDATE call_support_hours
            SET deleted_at = NOW()
            WHERE doctor_id = :doctorID
                AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("doctorID", callSupport.doctorID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
