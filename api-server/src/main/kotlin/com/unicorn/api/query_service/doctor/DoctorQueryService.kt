package com.unicorn.api.query_service.doctor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.hospital.HospitalID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

interface DoctorQueryService {
    fun getOrNullBy(doctorID: DoctorID): DoctorDto?
    fun getOrNullBy(hospitalID: HospitalID): DoctorResult
    fun searchDoctors(
        departmentID: String?,
        doctorName: String?,
        hospitalName: String?): DoctorResult
}

@Service
class DoctorQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
): DoctorQueryService {
    override fun getOrNullBy(doctorID: DoctorID): DoctorDto? {
        // language=postgresql
        val sql = """
            SELECT
                doctors.doctor_id,
                hospitals.hospital_id,
                hospitals.hospital_name,
                doctors.email,
                doctors.phone_number,
                doctors.first_name,
                doctors.last_name,
                doctors.doctor_icon_url,
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
            FROM doctors
            LEFT JOIN hospitals ON doctors.hospital_id = hospitals.hospital_id
            LEFT JOIN doctor_departments ON doctors.doctor_id = doctor_departments.doctor_id
            LEFT JOIN departments ON doctor_departments.department_id = departments.department_id
            LEFT JOIN call_support_hours ON doctors.doctor_id = call_support_hours.doctor_id
            LEFT JOIN chat_support_hours ON doctors.doctor_id = chat_support_hours.doctor_id
            WHERE doctors.doctor_id = :doctorID
            GROUP BY 
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
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("doctorID", doctorID.value)

        fun String.toHHMM() = this.split(":").take(2).joinToString(":")

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            DoctorDto(
                doctorID = rs.getString("doctor_id"),
                hospital = HospitalDto(
                    hospitalID = rs.getString("hospital_id"),
                    hospitalName = rs.getString("hospital_name")
                ),
                email = rs.getString("email"),
                phoneNumber = rs.getString("phone_number"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                doctorIconUrl = rs.getString("doctor_icon_url"),
                departments = jacksonObjectMapper().readValue<List<DepartmentDto>>(rs.getString("departments")),
                chatSupportHours = "${rs.getString("chat_support_start_time").toHHMM()}-${rs.getString("chat_support_end_time").toHHMM()}",
                callSupportHours = "${rs.getString("call_support_start_time").toHHMM()}-${rs.getString("call_support_end_time").toHHMM()}"
            )
        }.singleOrNull()
    }

    override fun getOrNullBy(hospitalID: HospitalID): DoctorResult {
        TODO("Not yet implemented")
    }

    override fun searchDoctors(departmentID: String?, doctorName: String?, hospitalName: String?): DoctorResult {
        TODO("Not yet implemented")
    }
}


data class HospitalDto(
    val hospitalID: String,
    val hospitalName: String
)

data class DepartmentDto(
    val departmentID: String,
    val departmentName: String
)

data class DoctorDto(
    val doctorID: String,
    val firstName: String,
    val lastName: String,
    val hospital: HospitalDto,
    val doctorIconUrl: String?,
    val departments: List<DepartmentDto>,
    val email: String,
    val phoneNumber: String,
    val chatSupportHours: String,
    val callSupportHours: String
)

data class DoctorResult(
    val data: List<DoctorDto>
)