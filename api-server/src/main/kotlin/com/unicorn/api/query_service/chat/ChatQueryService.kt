package com.unicorn.api.query_service.chat

import com.unicorn.api.domain.account.UID
import com.unicorn.api.util.toJST
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

interface ChatQueryService {
    fun getBy(uid: UID): ChatResult
}

@Service
class ChatQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : ChatQueryService {
    override fun getBy(uid: UID): ChatResult {
        // language=postgresql
        val sql =
            """
            WITH latest_messages AS (
                SELECT
                    chat_id,
                    content AS latest_message_text,
                    sent_at AS latest_message_sent_at
                FROM
                    messages
                WHERE
                    (chat_id, sent_at) IN (
                        SELECT
                            chat_id,
                            MAX(sent_at) AS sent_at
                        FROM
                            messages
                        WHERE deleted_at IS NULL
                        GROUP BY
                            chat_id
                    )
            )
            SELECT
                chats.chat_id,
                users.user_id,
                users.icon_image_url AS user_icon_url,
                users.first_name AS user_first_name,
                users.last_name AS user_last_name,
                doctors.doctor_id,
                doctors.doctor_icon_url AS doctor_icon_url,
                doctors.first_name AS doctor_first_name,
                doctors.last_name AS doctor_last_name,
                latest_messages.latest_message_text,
                latest_messages.latest_message_sent_at
            FROM
                chats
            INNER JOIN users ON chats.user_id = users.user_id AND users.deleted_at IS NULL
            INNER JOIN doctors ON chats.doctor_id = doctors.doctor_id AND doctors.deleted_at IS NULL
            LEFT JOIN latest_messages ON chats.chat_id = latest_messages.chat_id
            WHERE
                chats.user_id = :uid
                OR chats.doctor_id = :uid
            ORDER BY
                latest_messages.latest_message_sent_at DESC;
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("uid", uid.value)

        return ChatResult(
            data =
                namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                    ChatDto(
                        chatID = UUID.fromString(rs.getString("chat_id")),
                        doctor =
                            ChatDoctorDto(
                                doctorID = rs.getString("doctor_id"),
                                doctorIconUrl = rs.getString("doctor_icon_url"),
                                firstName = rs.getString("doctor_first_name"),
                                lastName = rs.getString("doctor_last_name"),
                            ),
                        user =
                            ChatUserDto(
                                userID = rs.getString("user_id"),
                                userIconUrl = rs.getString("user_icon_url"),
                                firstName = rs.getString("user_first_name"),
                                lastName = rs.getString("user_last_name"),
                            ),
                        latestMessageText = rs.getString("latest_message_text"),
                        latestMessageSentAt =
                            rs.getObject("latest_message_sent_at", OffsetDateTime::class.java)
                                ?.toJST(),
                    )
                },
        )
    }
}

data class ChatDto(
    val chatID: UUID,
    val doctor: ChatDoctorDto,
    val user: ChatUserDto,
    val latestMessageText: String?,
    val latestMessageSentAt: OffsetDateTime?,
)

data class ChatResult(
    val data: List<ChatDto>,
)

data class ChatDoctorDto(
    val doctorID: String,
    val doctorIconUrl: String?,
    val firstName: String,
    val lastName: String,
)

data class ChatUserDto(
    val userID: String,
    val userIconUrl: String?,
    val firstName: String,
    val lastName: String,
)
