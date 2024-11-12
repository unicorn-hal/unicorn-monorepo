package com.unicorn.api.infrastructure.chat_support

import com.unicorn.api.domain.chat_support.ChatSupport
import com.unicorn.api.domain.doctor.DoctorID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface ChatSupportRepository {
    fun store(chatSupport: ChatSupport): ChatSupport

    fun getOrNullBy(doctorID: DoctorID): ChatSupport?

    fun delete(chatSupport: ChatSupport): Unit
}

@Repository
class ChatSupportRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : ChatSupportRepository {
    override fun getOrNullBy(doctorID: DoctorID): ChatSupport? {
        // language=postgresql
        val sql =
            """
            SELECT 
                chat_support_id,
                doctor_id,
                start_time,
                end_time
            FROM chat_support_hours
            WHERE doctor_id = :doctorID
                AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("doctorID", doctorID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            ChatSupport.fromStore(
                chatSupportID = UUID.fromString(rs.getString("chat_support_id")),
                doctorID = rs.getString("doctor_id"),
                chatSupportStartHour = rs.getTime("start_time").toLocalTime(),
                chatSupportEndHour = rs.getTime("end_time").toLocalTime(),
            )
        }.singleOrNull()
    }

    override fun store(chatSupport: ChatSupport): ChatSupport {
        // language=postgresql
        val sql =
            """
            INSERT INTO chat_support_hours (
                chat_support_id, 
                doctor_id, 
                start_time, 
                end_time, 
                created_at
            ) VALUES (
                :chatSupportID,
                :doctorID,
                :startTime,
                :endTime,
                NOW()
            )
            ON CONFLICT (chat_support_id) DO UPDATE 
            SET 
                start_time = EXCLUDED.start_time,
                end_time = EXCLUDED.end_time
            WHERE chat_support_hours.created_at IS NOT NULL
            
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("chatSupportID", chatSupport.chatSupportID.value)
                .addValue("doctorID", chatSupport.doctorID.value)
                .addValue("startTime", chatSupport.chatSupportStartHour.value)
                .addValue("endTime", chatSupport.chatSupportEndHour.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)

        return chatSupport
    }

    override fun delete(chatSupport: ChatSupport) {
        // language=postgresql
        val sql =
            """
            UPDATE chat_support_hours
            SET deleted_at = NOW()
            WHERE chat_support_id = :chatSupportID
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("chatSupportID", chatSupport.chatSupportID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
