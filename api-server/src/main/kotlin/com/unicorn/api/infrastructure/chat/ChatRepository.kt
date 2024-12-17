package com.unicorn.api.infrastructure.chat

import com.unicorn.api.domain.chat.Chat
import com.unicorn.api.domain.chat.ChatID
import com.unicorn.api.domain.user.User
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface ChatRepository {
    fun store(chat: Chat): Chat

    fun getOrNullBy(chatID: ChatID): Chat?

    fun delete(chat: Chat)

    fun deleteByUser(user: User)
}

@Repository
class ChatRepositoryImpl(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : ChatRepository {
    override fun store(chat: Chat): Chat {
        // language=postgresql
        val sql =
            """
            INSERT INTO chats (
                chat_id, 
                doctor_id, 
                user_id
            ) VALUES (
                :chatID, 
                :doctorID, 
                :userID
            )
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("chatID", chat.chatID.value)
                .addValue("doctorID", chat.doctorID.value)
                .addValue("userID", chat.userID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)

        return chat
    }

    override fun getOrNullBy(chatID: ChatID): Chat? {
        // language=postgresql
        val sql =
            """
            SELECT
                chat_id,
                doctor_id,
                user_id
            FROM chats
            WHERE chat_id = :chatID
                AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("chatID", chatID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            Chat.fromStore(
                chatID = UUID.fromString(rs.getString("chat_id")),
                doctorID = rs.getString("doctor_id"),
                userID = rs.getString("user_id"),
            )
        }.singleOrNull()
    }

    override fun delete(chat: Chat) {
        // language=postgresql
        val sql =
            """
            UPDATE chats
            SET deleted_at = NOW()
            WHERE chat_id = :chatID
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("chatID", chat.chatID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }

    override fun deleteByUser(user: User) {
        // language=postgresql
        val sql =
            """
            UPDATE chats
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
