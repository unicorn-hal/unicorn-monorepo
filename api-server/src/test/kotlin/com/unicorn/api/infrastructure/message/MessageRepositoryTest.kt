package com.unicorn.api.infrastructure.message

import com.unicorn.api.domain.message.Message
import com.unicorn.api.domain.message.MessageID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/account/Insert_Account_Data.sql")
@Sql("/db/hospital/Insert_Hospital_Data.sql")
@Sql("/db/department/Insert_Department_Data.sql")
@Sql("/db/doctor/Insert_Doctor_Data.sql")
@Sql("/db/doctor_department/Insert_Doctor_Department_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/chat/Insert_Chat_Data.sql")
@Sql("/db/message/Insert_Message_Data.sql")
@Sql("/db/message/Insert_Deleted_Message_Data.sql")
class MessageRepositoryTest {
    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findBy(messageID: MessageID): Message? {
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

    @Test
    fun `should store message`() {
        val chatID = UUID.fromString("e38fd3d0-99bc-11ef-8e52-cfa170f7b603")
        val messageID = UUID.randomUUID()
        val senderID = "test"
        val sentAt = OffsetDateTime.of(LocalDateTime.of(2021, 1, 10, 9, 0, 0), ZoneOffset.of("+00:00"))
        val content = "content"
        val message =
            Message.fromStore(
                messageID = messageID,
                chatID = chatID,
                senderID = senderID,
                sentAt = sentAt,
                content = content,
            )

        messageRepository.store(message)

        val storedMessage = findBy(message.messageID)
        assertEquals(message, storedMessage)
    }

    @Test
    fun `should get message by messageID`() {
        val messageID = UUID.fromString("66197db0-99bd-11ef-8e52-cfa170f7b603")
        val chatID = UUID.fromString("e38fd3d0-99bc-11ef-8e52-cfa170f7b603")
        val senderID = "test"
        val sentAt = OffsetDateTime.of(LocalDateTime.of(2021, 4, 1, 10, 0, 0), ZoneOffset.of("+00:00"))
        val content = "Hello"
        val message =
            Message.fromStore(
                messageID = messageID,
                chatID = chatID,
                senderID = senderID,
                sentAt = sentAt,
                content = content,
            )

        val result = messageRepository.getOrNullBy(MessageID(messageID))

        assertEquals(message, result)
    }

    @Test
    fun `should delete message`() {
        val messageID = UUID.fromString("66197db0-99bd-11ef-8e52-cfa170f7b603")
        val chatID = UUID.fromString("e38fd3d0-99bc-11ef-8e52-cfa170f7b603")
        val senderID = "test"
        val sentAt = OffsetDateTime.of(LocalDateTime.of(2021, 1, 1, 9, 0, 0), ZoneOffset.of("+00:00"))
        val content = "Hello"
        val message =
            Message.fromStore(
                messageID = messageID,
                chatID = chatID,
                senderID = senderID,
                sentAt = sentAt,
                content = content,
            )

        messageRepository.delete(message)

        val deletedMessage = findBy(MessageID(messageID))
        assertEquals(null, deletedMessage)
    }
}
