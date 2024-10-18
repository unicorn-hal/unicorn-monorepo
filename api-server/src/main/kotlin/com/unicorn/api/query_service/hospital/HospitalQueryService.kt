package com.unicorn.api.application.hospital

import org.springframework.stereotype.Service
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.util.UUID

interface HospitalQueryService {
    fun getHospitals(): HospitalResult
    fun getBy(hospitalID: UUID): HospitalDto?
}

@Service
class HospitalQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : HospitalQueryService {

    override fun getHospitals(): HospitalResult {
        val sql = """
            SELECT
                hospital_id,
                hospital_name,
                address,
                postal_code,
                phone_number
            FROM hospitals
            WHERE deleted_at IS NULL
        """.trimIndent()

        val hospitals = namedParameterJdbcTemplate.query(
            sql,
            { rs, _ ->
                HospitalDto(
                    hospitalID = UUID.fromString(rs.getString("hospital_id")),
                    hospitalName = rs.getString("hospital_name"),
                    address = rs.getString("address"),
                    postalCode = rs.getString("postal_code"),
                    phoneNumber = rs.getString("phone_number")
                )
            }
        )

        return HospitalResult(hospitals)
    }

    override fun getBy(hospitalID: UUID): HospitalDto? {

        val sql = """
            SELECT
                hospital_id,
                hospital_name,
                address,
                postal_code,
                phone_number
            FROM hospitals
            WHERE hospital_id = :hospitalID
            AND deleted_at IS NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("hospitalID", hospitalID)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams
        ) { rs, _ ->
            HospitalDto(
                hospitalID = UUID.fromString(rs.getString("hospital_id")),
                hospitalName = rs.getString("hospital_name"),
                address = rs.getString("address"),
                postalCode = rs.getString("postal_code"),
                phoneNumber = rs.getString("phone_number")
            )
        }.singleOrNull()
    }
}

data class HospitalResult(
    val data: List<HospitalDto>
)

data class HospitalDto(
    val hospitalID: UUID,
    val hospitalName: String,
    val address: String,
    val postalCode: String,
    val phoneNumber: String
)
