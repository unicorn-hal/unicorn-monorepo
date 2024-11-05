package com.unicorn.api.query_service.message

import com.unicorn.api.domain.account.Role
import com.unicorn.api.domain.chat.ChatID
import com.unicorn.api.domain.message.MessageID
import com.unicorn.api.util.toJST
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

interface MessageQueryService {
    fun getBy(chatID: ChatID): MessageResult

    fun getOrNullBy(messageID: MessageID): MessageDTO?
}

@Service
class MessageQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : MessageQueryService {
    override fun getBy(chatID: ChatID): MessageResult {
        // language=postgresql
        val sql =
            """
            SELECT
                 messages.message_id,
                 a.role,
                 messages.chat_id,
                 messages.sender_id,
                 messages.content,
                 messages.sent_at,
                 u.first_name AS user_first_name,
                 u.last_name AS user_last_name,
                 u.icon_image_url AS user_icon_image_url,
                 d.first_name AS doctor_first_name,
                 d.last_name AS doctor_last_name,
                 d.doctor_icon_url AS doctor_icon_image_url
            FROM messages
            LEFT JOIN accounts a on a.uid = messages.sender_id
            LEFT JOIN doctors d on a.uid = d.doctor_id
            LEFT JOIN users u on a.uid = u.user_id
            WHERE chat_id = :chatID
            ORDER BY sent_at
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("chatID", chatID.value)

        val data =
            namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                val role = Role.valueOf(rs.getString("role"))
                when (role) {
                    Role.user ->
                        MessageDTO(
                            messageID = rs.getString("message_id"),
                            chatID = rs.getString("chat_id"),
                            senderID = rs.getString("sender_id"),
                            firstName = rs.getString("user_first_name"),
                            lastName = rs.getString("user_last_name"),
                            iconImageUrl = rs.getString("user_icon_image_url"),
                            content = rs.getString("content"),
                            sentAt = rs.getObject("sent_at", OffsetDateTime::class.java).toJST(),
                        )
                    Role.doctor ->
                        MessageDTO(
                            messageID = rs.getString("message_id"),
                            chatID = rs.getString("chat_id"),
                            senderID = rs.getString("sender_id"),
                            firstName = rs.getString("doctor_first_name"),
                            lastName = rs.getString("doctor_last_name"),
                            iconImageUrl = rs.getString("doctor_icon_image_url"),
                            content = rs.getString("content"),
                            sentAt = rs.getObject("sent_at", OffsetDateTime::class.java).toJST(),
                        )
                }
            }

        return MessageResult(data)
    }

    override fun getOrNullBy(messageID: MessageID): MessageDTO? {
        // language=postgresql
        val sql =
            """
            SELECT
                 messages.message_id,
                 a.role,
                 messages.chat_id,
                 messages.sender_id,
                 messages.content,
                 messages.sent_at,
                 u.first_name AS user_first_name,
                 u.last_name AS user_last_name,
                 u.icon_image_url AS user_icon_image_url,
                 d.first_name AS doctor_first_name,
                 d.last_name AS doctor_last_name,
                 d.doctor_icon_url AS doctor_icon_image_url
            FROM messages
            LEFT JOIN accounts a on a.uid = messages.sender_id
            LEFT JOIN doctors d on a.uid = d.doctor_id
            LEFT JOIN users u on a.uid = u.user_id
            WHERE message_id = :messageID
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("messageID", messageID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            val role = Role.valueOf(rs.getString("role"))
            when (role) {
                Role.user ->
                    MessageDTO(
                        messageID = rs.getString("message_id"),
                        chatID = rs.getString("chat_id"),
                        senderID = rs.getString("sender_id"),
                        firstName = rs.getString("user_first_name"),
                        lastName = rs.getString("user_last_name"),
                        iconImageUrl = rs.getString("user_icon_image_url"),
                        content = rs.getString("content"),
                        sentAt = rs.getObject("sent_at", OffsetDateTime::class.java),
                    )
                Role.doctor ->
                    MessageDTO(
                        messageID = rs.getString("message_id"),
                        chatID = rs.getString("chat_id"),
                        senderID = rs.getString("sender_id"),
                        firstName = rs.getString("doctor_first_name"),
                        lastName = rs.getString("doctor_last_name"),
                        iconImageUrl = rs.getString("doctor_icon_image_url"),
                        content = rs.getString("content"),
                        sentAt = rs.getObject("sent_at", OffsetDateTime::class.java).toJST(),
                    )
            }
        }.singleOrNull()
    }
}

data class MessageDTO(
    val messageID: String,
    val chatID: String,
    val senderID: String,
    val firstName: String,
    val lastName: String,
    val iconImageUrl: String?,
    val content: String,
    val sentAt: OffsetDateTime,
)

data class MessageResult(
    val data: List<MessageDTO>,
)
