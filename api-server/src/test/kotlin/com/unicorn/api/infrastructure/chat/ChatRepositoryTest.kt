package com.unicorn.api.infrastructure.chat

import com.unicorn.api.domain.chat.Chat
import com.unicorn.api.domain.chat.ChatID
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

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
class ChatRepositoryTest {
    @Autowired
    private lateinit var chatRepository: ChatRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findBy(chatID: ChatID): Chat? {
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

    @Test
    fun `should store chat`() {
        val doctorID = "doctor"
        val userID = "test"
        val chat = Chat.create(DoctorID(doctorID), UserID(userID))

        chatRepository.store(chat)

        val result = findBy(chat.chatID)
        assertEquals(chat, result)
    }

    @Test
    fun `should get chat by chatID`() {
        val chatID = UUID.fromString("e38fd3d0-99bc-11ef-8e52-cfa170f7b603")
        val doctorID = "doctor"
        val userID = "test"
        val chat = Chat.fromStore(chatID, doctorID, userID)

        val result = chatRepository.getOrNullBy(ChatID(chatID))

        assertEquals(chat, result)
    }

    @Test
    fun `should delete chat`() {
        val chatID = UUID.fromString("e38fd3d0-99bc-11ef-8e52-cfa170f7b603")
        val doctorID = "doctor"
        val userID = "test"
        val chat = Chat.fromStore(chatID, doctorID, userID)

        chatRepository.delete(chat)

        val result = findBy(ChatID(chatID))
        assertEquals(null, result)
    }
}
