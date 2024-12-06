package com.unicorn.api.query_service.call

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.unicorn.api.domain.call.CallReservationID
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.query_service.doctor.DepartmentDto
import com.unicorn.api.query_service.doctor.DoctorDto
import com.unicorn.api.query_service.doctor.HospitalDto
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

interface CallQueryService {
    fun get(
        doctorID: DoctorID,
        userID: UserID,
    ): CallResult

    fun getByUserID(userID: UserID): CallGetByUserIDResult

    fun getByDoctorID(doctorID: DoctorID): CallResult

    fun getOrNullBy(callReservationID: CallReservationID): CallDto?
}

@Service
class CallQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : CallQueryService {
    fun String.toHHMM() = this.split(":").take(2).joinToString(":")

    override fun get(
        doctorID: DoctorID,
        userID: UserID,
    ): CallResult {
        //language=postgresql
        val sql =
            """
            SELECT
                call_reservation_id,
                doctor_id,
                user_id,
                call_start_time,
                call_end_time
            FROM call_reservations
            WHERE doctor_id = :doctor_id
            AND user_id = :user_id
            AND call_end_time >= CURRENT_TIMESTAMP
            AND deleted_at IS NULL
            ORDER BY call_start_time ASC
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("doctor_id", doctorID.value)
                .addValue("user_id", userID.value)
        val calls =
            namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                CallDto(
                    callReservationID = UUID.fromString(rs.getString("call_reservation_id")),
                    doctorID = rs.getString("doctor_id"),
                    userID = rs.getString("user_id"),
                    callStartTime = rs.getObject("call_start_time", OffsetDateTime::class.java),
                    callEndTime = rs.getObject("call_end_time", OffsetDateTime::class.java),
                )
            }
        return CallResult(calls)
    }

    override fun getByUserID(userID: UserID): CallGetByUserIDResult {
        //language=postgresql
        val sql =
            """
            SELECT
                call_reservations.call_reservation_id,
                call_reservations.doctor_id,
                call_reservations.user_id,
                call_reservations.call_start_time,
                call_reservations.call_end_time,
                doctors.doctor_id AS doctor_id,
                hospitals.hospital_id AS hospital_id,
                hospitals.hospital_name AS hospital_name,
                doctors.email AS email,
                doctors.phone_number AS phone_number,
                doctors.first_name AS first_name,
                doctors.last_name AS last_name,
                doctors.doctor_icon_url AS doctor_icon_url,
                COALESCE(
                    JSONB_AGG(
                        JSONB_BUILD_OBJECT(
                            'departmentID', departments.department_id,
                            'departmentName', departments.department_name
                        )
                    ) FILTER (WHERE departments.department_id IS NOT NULL),
                    '[]'
                ) AS departments,
                chat_support_hours.start_time AS chat_support_start_time,
                chat_support_hours.end_time AS chat_support_end_time,
                call_support_hours.start_time AS call_support_start_time,
                call_support_hours.end_time AS call_support_end_time
            FROM call_reservations
            LEFT JOIN doctors ON call_reservations.doctor_id = doctors.doctor_id
            LEFT JOIN hospitals ON doctors.hospital_id = hospitals.hospital_id
            LEFT JOIN doctor_departments ON doctors.doctor_id = doctor_departments.doctor_id
                AND doctor_departments.deleted_at IS NULL
            LEFT JOIN departments ON doctor_departments.department_id = departments.department_id
            LEFT JOIN call_support_hours ON doctors.doctor_id = call_support_hours.doctor_id
            LEFT JOIN chat_support_hours ON doctors.doctor_id = chat_support_hours.doctor_id
            WHERE call_reservations.user_id = :user_id
                AND call_reservations.call_end_time >= CURRENT_TIMESTAMP
                AND call_reservations.call_end_time <= CURRENT_TIMESTAMP + INTERVAL '1 YEAR'
                AND call_reservations.deleted_at IS NULL
                AND doctors.deleted_at IS NULL
            GROUP BY 
                call_reservations.call_reservation_id,
                call_reservations.doctor_id,
                call_reservations.user_id,
                call_reservations.call_start_time,
                call_reservations.call_end_time,
                doctors.doctor_id,
                hospitals.hospital_id,
                hospitals.hospital_name,
                doctors.email,
                doctors.phone_number,
                doctors.first_name,
                doctors.last_name,
                doctors.doctor_icon_url,
                chat_support_hours.start_time,
                chat_support_hours.end_time,
                call_support_hours.start_time,
                call_support_hours.end_time
            ORDER BY call_reservations.call_start_time ASC
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("user_id", userID.value)
        val calls =
            namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                CallGetByUserIDDto(
                    callReservationID = UUID.fromString(rs.getString("call_reservation_id")),
                    doctor =
                        DoctorDto(
                            doctorID = rs.getString("doctor_id"),
                            hospital =
                                HospitalDto(
                                    hospitalID = rs.getString("hospital_id"),
                                    hospitalName = rs.getString("hospital_name"),
                                ),
                            email = rs.getString("email"),
                            phoneNumber = rs.getString("phone_number"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            doctorIconUrl = rs.getString("doctor_icon_url"),
                            departments = jacksonObjectMapper().readValue<List<DepartmentDto>>(rs.getString("departments")),
                            chatSupportHours = "${rs.getString(
                                "chat_support_start_time",
                            ).toHHMM()}-${rs.getString("chat_support_end_time").toHHMM()}",
                            callSupportHours = "${rs.getString(
                                "call_support_start_time",
                            ).toHHMM()}-${rs.getString("call_support_end_time").toHHMM()}",
                        ),
                    userID = rs.getString("user_id"),
                    callStartTime = rs.getObject("call_start_time", OffsetDateTime::class.java),
                    callEndTime = rs.getObject("call_end_time", OffsetDateTime::class.java),
                )
            }
        return CallGetByUserIDResult(calls)
    }

    override fun getByDoctorID(doctorID: DoctorID): CallResult {
        //language=postgresql
        val sql =
            """
            SELECT
                call_reservation_id,
                doctor_id,
                user_id,
                call_start_time,
                call_end_time
            FROM call_reservations
            WHERE doctor_id = :doctor_id
            AND call_end_time >= CURRENT_TIMESTAMP
            AND call_end_time <= CURRENT_TIMESTAMP + INTERVAL '1 YEAR'
            AND deleted_at IS NULL
            ORDER BY call_start_time ASC
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("doctor_id", doctorID.value)
        val calls =
            namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                CallDto(
                    callReservationID = UUID.fromString(rs.getString("call_reservation_id")),
                    doctorID = rs.getString("doctor_id"),
                    userID = rs.getString("user_id"),
                    callStartTime = rs.getObject("call_start_time", OffsetDateTime::class.java),
                    callEndTime = rs.getObject("call_end_time", OffsetDateTime::class.java),
                )
            }
        return CallResult(calls)
    }

    override fun getOrNullBy(callReservationID: CallReservationID): CallDto? {
        //language=postgresql
        val sql =
            """
            SELECT
                call_reservation_id,
                doctor_id,
                user_id,
                call_start_time,
                call_end_time
            FROM call_reservations
            WHERE call_reservation_id = :call_reservation_id
            AND deleted_at IS NULL
            """.trimIndent()
        val sqlParams =
            MapSqlParameterSource()
                .addValue("call_reservation_id", callReservationID.value)
        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            CallDto(
                callReservationID = UUID.fromString(rs.getString("call_reservation_id")),
                doctorID = rs.getString("doctor_id"),
                userID = rs.getString("user_id"),
                callStartTime = rs.getObject("call_start_time", OffsetDateTime::class.java),
                callEndTime = rs.getObject("call_end_time", OffsetDateTime::class.java),
            )
        }.singleOrNull()
    }
}

data class CallDto(
    val callReservationID: UUID,
    val doctorID: String,
    val userID: String,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    var callStartTime: OffsetDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    var callEndTime: OffsetDateTime,
) {
    init {
        val jstOffset = ZoneOffset.ofHours(9)
        callStartTime = callStartTime.withOffsetSameInstant(jstOffset)
        callEndTime = callEndTime.withOffsetSameInstant(jstOffset)
    }
}

data class CallResult(
    val data: List<CallDto>,
)

data class CallGetByUserIDDto(
    val callReservationID: UUID,
    val doctor: DoctorDto,
    val userID: String,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    var callStartTime: OffsetDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    var callEndTime: OffsetDateTime,
) {
    init {
        val jstOffset = ZoneOffset.ofHours(9)
        callStartTime = callStartTime.withOffsetSameInstant(jstOffset)
        callEndTime = callEndTime.withOffsetSameInstant(jstOffset)
    }
}

data class CallGetByUserIDResult(
    val data: List<CallGetByUserIDDto>,
)
