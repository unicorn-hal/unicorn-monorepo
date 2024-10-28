package com.unicorn.api.query_service.doctor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.unicorn.api.domain.department.DepartmentID
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.hospital.HospitalID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

interface DoctorQueryService {
    fun getOrNullBy(doctorID: DoctorID): DoctorDto?
    fun getBy(hospitalID: HospitalID): DoctorResult
    fun searchDoctors(
        departmentID: DepartmentID?,
        doctorName: String?,
        hospitalName: String?): DoctorResult
}

@Service
class DoctorQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
): DoctorQueryService {
    fun String.toHHMM() = this.split(":").take(2).joinToString(":")

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

    override fun getBy(hospitalID: HospitalID): DoctorResult {
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
            WHERE hospitals.hospital_id = :hospitalID
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
            .addValue("hospitalID", hospitalID.value)

        val result = namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
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
        }

        return DoctorResult(result)
    }

    override fun searchDoctors(departmentID: DepartmentID?, doctorName: String?, hospitalName: String?): DoctorResult {
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
                        DISTINCT JSONB_BUILD_OBJECT(
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
            WHERE (:doctorName::VARCHAR IS NULL OR (doctors.first_name || ' ' || doctors.last_name) ILIKE '%' || :doctorName::VARCHAR || '%')
                AND (:hospitalName::VARCHAR IS NULL OR hospitals.hospital_name ILIKE '%' || :hospitalName::VARCHAR || '%')
                AND (:departmentID::UUID IS NULL OR doctors.doctor_id IN (
                    SELECT dd.doctor_id
                    FROM doctor_departments dd
                    WHERE dd.department_id = :departmentID::UUID
                ))
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
            .addValue("departmentID", departmentID?.value)
            .addValue("doctorName", doctorName)
            .addValue("hospitalName", hospitalName)

        val result = namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
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
        }
        return DoctorResult(result)
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