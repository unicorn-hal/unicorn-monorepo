package com.unicorn.api.infrastructure.message

import com.unicorn.api.domain.message.Message
import com.unicorn.api.domain.message.MessageID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

interface MessageRepository {
    fun store(message: Message): Message

    fun getOrNullBy(messageID: MessageID): Message?

    fun delete(message: Message)
}

@Repository
class MessageRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : MessageRepository {
    override fun store(message: Message): Message {
        // language=postgresql
        val sql =
            """
            INSERT INTO messages (
                message_id,
                chat_id,
                sender_id,
                sent_at,
                content
            ) VALUES (
                :messageID,
                :chatID,
                :senderID,
                :sentAt,
                :content
            )
            ON CONFLICT (message_id)
            DO UPDATE SET
                chat_id = EXCLUDED.chat_id,
                sender_id = EXCLUDED.sender_id,
                sent_at = EXCLUDED.sent_at,
                content = EXCLUDED.content
            WHERE messages.created_at IS NOT NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("messageID", message.messageID.value)
                .addValue("chatID", message.chatID.value)
                .addValue("senderID", message.senderID.value)
                .addValue("sentAt", message.sentAt.value)
                .addValue("content", message.content.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)

        return message
    }

    override fun getOrNullBy(messageID: MessageID): Message? {
        // language=postgresql
        val sql =
            """
            SELECT
                message_id,
                chat_id,
                sender_id,
                sent_at,
                content
            FROM messages
            WHERE message_id = :messageID
                AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("messageID", messageID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            Message.fromStore(
                messageID = UUID.fromString(rs.getString("message_id")),
                chatID = UUID.fromString(rs.getString("chat_id")),
                senderID = rs.getString("sender_id"),
                sentAt = rs.getObject("sent_at", OffsetDateTime::class.java),
                content = rs.getString("content"),
            )
        }.singleOrNull()
    }

    override fun delete(message: Message) {
        // language=postgresql
        val sql =
            """
            UPDATE messages
            SET deleted_at = NOW()
            WHERE message_id = :messageID
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("messageID", message.messageID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
