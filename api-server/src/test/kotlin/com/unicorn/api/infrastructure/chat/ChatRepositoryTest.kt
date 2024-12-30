package com.unicorn.api.infrastructure.chat

import com.unicorn.api.domain.chat.Chat
import com.unicorn.api.domain.chat.ChatID
import com.unicorn.api.domain.doctor.Doctor
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.User
import com.unicorn.api.domain.user.UserID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
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

    private fun findByUserID(userID: UserID): List<Chat> {
        // language=postgresql
        val sql =
            """
            SELECT
                chat_id, 
                doctor_id, 
                user_id
            FROM chats
            WHERE user_id = :userID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", userID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            Chat.fromStore(
                chatID = UUID.fromString(rs.getString("chat_id")),
                doctorID = rs.getString("doctor_id"),
                userID = rs.getString("user_id"),
            )
        }
    }

    private fun findByDoctorID(doctorID: DoctorID): List<Chat> {
        // language=postgresql
        val sql =
            """
            SELECT
                chat_id, 
                doctor_id, 
                user_id
            FROM chats
            WHERE doctor_id = :doctorID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("doctorID", doctorID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            Chat.fromStore(
                chatID = UUID.fromString(rs.getString("chat_id")),
                doctorID = rs.getString("doctor_id"),
                userID = rs.getString("user_id"),
            )
        }
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

    @Test
    fun `should delete chat by user`() {
        val user =
            User.fromStore(
                userID = "test",
                firstName = "test",
                lastName = "test",
                email = "sample@test.com",
                birthDate = LocalDate.of(1990, 1, 1),
                gender = "male",
                address = "test",
                postalCode = "0000000",
                phoneNumber = "00000000000",
                iconImageUrl = "https://example.com",
                bodyHeight = 170.4,
                bodyWeight = 60.4,
                occupation = "test",
            )

        chatRepository.deleteByUser(user)

        val result = findByUserID(user.userID)
        assertEquals(0, result.size)
    }

    @Test
    fun `should delete chat by doctor`() {
        val doctor =
            Doctor.fromStore(
                doctorID = "doctor",
                hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
                firstName = "test",
                lastName = "test",
                email = "test@test.com",
                phoneNumber = "1234567890",
                doctorIconUrl = "https://example.com",
                departments = listOf(UUID.fromString("b68a87a3-b7f1-4b85-b0ab-6c620d68d791")),
            )
        chatRepository.deleteByDoctor(doctor)

        val result = findByDoctorID(doctor.doctorID)
        assertEquals(0, result.size)
    }
}
