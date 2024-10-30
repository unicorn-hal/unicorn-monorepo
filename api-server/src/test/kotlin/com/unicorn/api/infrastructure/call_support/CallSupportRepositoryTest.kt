package com.unicorn.api.infrastructure.call_support

import com.unicorn.api.domain.call_support.CallSupport
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.infrastructure.chat_support.CallSupportRepository
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
@Sql("/db/call_support/Insert_Parent_Doctor_Data.sql")
@Sql("/db/call_support/Insert_Call_Support_Data.sql")
class CallSupportRepositoryTest {
    @Autowired
    private lateinit var callSupportRepository: CallSupportRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findByDoctorID(doctorID: String): CallSupport? {
        // language=postgresql
        val sql =
            """
            SELECT
                call_support_id,
                doctor_id,
                start_time,
                end_time
            FROM call_support_hours
            WHERE doctor_id = :doctorID
                AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("doctorID", doctorID)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            CallSupport.fromStore(
                callSupportID = UUID.fromString(rs.getString("call_support_id")),
                doctorID = rs.getString("doctor_id"),
                callSupportStartHour = rs.getTime("start_time").toLocalTime(),
                callSupportEndHour = rs.getTime("end_time").toLocalTime(),
            )
        }.singleOrNull()
    }

    @Test
    fun `should get call support by doctor id`() {
        val callSupport =
            CallSupport.fromStore(
                callSupportID = UUID.fromString("d1b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b2b"),
                doctorID = "doctor",
                callSupportStartHour = LocalTime.parse("09:00:00"),
                callSupportEndHour = LocalTime.parse("18:00:00"),
            )

        val act = callSupportRepository.getOrNullBy(callSupport.doctorID)

        assertEquals(callSupport, act)
    }

    @Test
    fun `should return null when call support not found`() {
        val act = callSupportRepository.getOrNullBy(DoctorID("not_found"))

        assertEquals(null, act)
    }

    @Test
    fun `should update call support`() {
        val callSupport =
            CallSupport.fromStore(
                callSupportID = UUID.fromString("d1b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b2b"),
                doctorID = "doctor",
                callSupportStartHour = LocalTime.parse("10:00:00"),
                callSupportEndHour = LocalTime.parse("18:00:00"),
            )

        callSupportRepository.store(callSupport)

        val act = findByDoctorID(callSupport.doctorID.value)
        assertEquals(callSupport, act)
    }

    @Test
    fun `should store call support`() {
        val doctorID = DoctorID("doctor2")
        val callSupportStartHour = LocalTime.of(9, 0)
        val callSupportEndHour = LocalTime.of(17, 0)

        val callSupport =
            CallSupport.create(
                doctorID,
                callSupportStartHour,
                callSupportEndHour,
            )

        callSupportRepository.store(callSupport)

        val act = findByDoctorID(doctorID.value)
        assertEquals(callSupport, act)
    }

    @Test
    fun `should delete call support`() {
        val callSupport =
            CallSupport.fromStore(
                callSupportID = UUID.fromString("d1b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b2b"),
                doctorID = "doctor",
                callSupportStartHour = LocalTime.parse("10:00:00"),
                callSupportEndHour = LocalTime.parse("18:00:00"),
            )

        callSupportRepository.delete(callSupport)

        val act = findByDoctorID(callSupport.doctorID.value)
        assertEquals(null, act)
    }
}
