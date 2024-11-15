package com.unicorn.api.infrastructure.call

import com.unicorn.api.domain.call.Call
import com.unicorn.api.util.toJST
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
@Sql("/db/call/Insert_Parent_Account_Data.sql")
@Sql("/db/call/Insert_Parent_User_Data.sql")
@Sql("/db/call/Insert_Parent_Hospital_Data.sql")
@Sql("/db/call/Insert_Parent_Doctor_Data.sql")
@Sql("/db/call/Insert_Call_Data.sql")
class CallRepositoryTest {
    @Autowired
    private lateinit var callRepository: CallRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findBy(callReservationID: UUID): Call? {
        val sql =
            """
            SELECT
                call_reservation_id,
                doctor_id,
                user_id,
                call_start_time,
                call_end_time
            FROM call_reservations WHERE call_reservation_id = :callReservationID AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams = MapSqlParameterSource().addValue("callReservationID", callReservationID)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams,
        ) { rs, _ ->
            Call.fromStore(
                callReservationID = UUID.fromString(rs.getString("call_reservation_id")),
                doctorID = rs.getString("doctor_id"),
                userID = rs.getString("user_id"),
                callStartTime = rs.getObject("call_start_time", OffsetDateTime::class.java).toJST(),
                callEndTime = rs.getObject("call_end_time", OffsetDateTime::class.java).toJST(),
            )
        }.singleOrNull()
    }

    @Test
    fun `should store call reservation`() {
        val call =
            Call.create(
                doctorID = "12345",
                userID = "12345",
                callStartTime = OffsetDateTime.of(LocalDateTime.of(2021, 1, 1, 9, 0, 0), ZoneOffset.of("+09:00")),
                callEndTime = OffsetDateTime.of(LocalDateTime.of(2021, 1, 1, 9, 30, 0), ZoneOffset.of("+09:00")),
            )

        callRepository.store(call)

        val storedCall = findBy(call.callReservationID.value)
        assert(storedCall?.callReservationID == call.callReservationID)
        assert(storedCall?.doctorID == call.doctorID)
        assert(storedCall?.userID == call.userID)
        assertEquals(call.callStartTime, storedCall?.callStartTime)
        assertEquals(call.callEndTime, storedCall?.callEndTime)
    }

    @Test
    fun `should get call reservation by callReservationID`() {
        val callReservationID = UUID.fromString("211177ed-92f8-a956-825f-c31b2cad8b15")
        val call =
            Call.fromStore(
                callReservationID = callReservationID,
                doctorID = "12345",
                userID = "12345",
                callStartTime = OffsetDateTime.of(LocalDateTime.of(2021, 1, 1, 9, 0, 0), ZoneOffset.of("+09:00")),
                callEndTime = OffsetDateTime.of(LocalDateTime.of(2021, 1, 1, 9, 30, 0), ZoneOffset.of("+09:00")),
            )

        val getCall = callRepository.getOrNullBy(call.callReservationID)

        assertEquals(call, getCall)
    }

    @Test
    fun `should update call reservation`() {
        val call =
            Call.create(
                doctorID = "12345",
                userID = "12345",
                callStartTime = OffsetDateTime.of(LocalDateTime.of(2021, 1, 1, 9, 0, 0), ZoneOffset.of("+09:00")),
                callEndTime = OffsetDateTime.of(LocalDateTime.of(2021, 1, 1, 9, 30, 0), ZoneOffset.of("+09:00")),
            )

        callRepository.store(call)

        val storedCall = findBy(call.callReservationID.value)
        assert(storedCall?.doctorID == call.doctorID)
        assert(storedCall?.userID == call.userID)
        assertEquals(call.callStartTime, storedCall?.callStartTime)
        assertEquals(call.callEndTime, storedCall?.callEndTime)
    }

    @Test
    fun `should delete call reservation`() {
        val call =
            Call.create(
                doctorID = "12345",
                userID = "12345",
                callStartTime = OffsetDateTime.of(LocalDateTime.of(2021, 1, 1, 9, 0, 0), ZoneOffset.of("+09:00")),
                callEndTime = OffsetDateTime.of(LocalDateTime.of(2021, 1, 1, 9, 30, 0), ZoneOffset.of("+09:00")),
            )

        callRepository.delete(call)

        val deletedCall = findBy(call.callReservationID.value)
        assertEquals(null, deletedCall)
    }
}
