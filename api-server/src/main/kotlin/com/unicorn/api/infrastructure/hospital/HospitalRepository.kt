package com.unicorn.api.infrastructure.hospital

import com.unicorn.api.domain.hospital.Hospital
import com.unicorn.api.domain.hospital.HospitalID
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface HospitalRepository {
    fun getOrNullBy(hospitalID: HospitalID): Hospital?
}

@Repository
class HospitalRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : HospitalRepository {
    override fun getOrNullBy(hospitalID: HospitalID): Hospital? {
        // language=postgresql
        val sql = """
            SELECT
                hospital_id,
                hospital_name,
                address,
                postal_code,
                phone_number
            FROM
                hospitals
            WHERE
                hospital_id = :hospitalID
                AND deleted_at IS NULL
        """.trimIndent()

        val sqlParam = MapSqlParameterSource()
            .addValue("hospitalID", hospitalID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParam) { rs, _ ->
            Hospital.fromStore(
                hospitalID = UUID.fromString(rs.getString("hospital_id")),
                hospitalName = rs.getString("hospital_name"),
                address = rs.getString("address"),
                postalCode = rs.getString("postal_code"),
                phoneNumber = rs.getString("phone_number")
            )
        }.singleOrNull()
    }
}