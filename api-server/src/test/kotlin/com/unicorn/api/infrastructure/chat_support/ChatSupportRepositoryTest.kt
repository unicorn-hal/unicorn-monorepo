package com.unicorn.api.infrastructure.chat_support

import com.unicorn.api.domain.chat_support.ChatSupport
import com.unicorn.api.domain.doctor.Doctor
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.infrastructure.call_support.ChatSupportRepository
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
import java.time.LocalTime
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
@Sql("/db/chat_support/Insert_Parent_Doctor_Data.sql")
@Sql("/db/chat_support/Insert_Chat_Support_Data.sql")
class ChatSupportRepositoryTest {
    @Autowired
    private lateinit var chatSupportRepository: ChatSupportRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findByDoctorID(doctorID: String): ChatSupport? {
        // language=postgresql
        val sql = """
            SELECT 
                chat_support_id,
                doctor_id,
                start_time,
                end_time
            FROM chat_support_hours
            WHERE doctor_id = :doctorID
                AND deleted_at IS NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("doctorID", doctorID)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            ChatSupport.fromStore(
                chatSupportID = UUID.fromString(rs.getString("chat_support_id")),
                doctorID = rs.getString("doctor_id"),
                chatSupportStartHour = LocalTime.parse(rs.getString("start_time")),
                chatSupportEndHour = LocalTime.parse(rs.getString("end_time"))
            )
        }.singleOrNull()
    }

    @Test
    fun `should get chat support by doctor id`() {
        val chatSupport = ChatSupport.fromStore(
            chatSupportID = UUID.fromString("d1b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b3b"),
            doctorID = "doctor",
            chatSupportStartHour = LocalTime.parse("09:00:00"),
            chatSupportEndHour = LocalTime.parse("18:00:00")
        )
        val act = chatSupportRepository.getOrNullBy(chatSupport.doctorID)

        assertEquals(chatSupport, act)
    }

    @Test
    fun `should return null when chat support not found`() {
        val act = chatSupportRepository.getOrNullBy(DoctorID("not_found"))

        assertEquals(null, act)
    }

    @Test
    fun `should update chat support`() {
        val chatSupport = ChatSupport.fromStore(
            chatSupportID = UUID.fromString("d1b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b3b"),
            doctorID = "doctor",
            chatSupportStartHour = LocalTime.parse("10:00:00"),
            chatSupportEndHour = LocalTime.parse("19:00:00")
        )

        chatSupportRepository.store(chatSupport)

        val act = findByDoctorID(chatSupport.doctorID.value)

        assertEquals(chatSupport, act)
    }

    @Test
    fun `should store chat support`() {
        val chatSupport = ChatSupport.create(
            doctorID = DoctorID("doctor2"),
            chatSupportStartHour = LocalTime.parse("10:00:00"),
            chatSupportEndHour = LocalTime.parse("19:00:00")
        )

        chatSupportRepository.store(chatSupport)

        val act = findByDoctorID(chatSupport.doctorID.value)
        assertEquals(chatSupport, act)
    }

    @Test
    fun `should delete chat support`() {
        val chatSupport = ChatSupport.fromStore(
            chatSupportID = UUID.fromString("d1b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b3b"),
            doctorID = "doctor",
            chatSupportStartHour = LocalTime.parse("10:00:00"),
            chatSupportEndHour = LocalTime.parse("19:00:00")
        )

        chatSupportRepository.delete(chatSupport)

        val act = findByDoctorID(chatSupport.doctorID.value)
        assertEquals(null, act)
    }
}