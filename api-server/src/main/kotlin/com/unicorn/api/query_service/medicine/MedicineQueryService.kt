package com.unicorn.api.query_service.medicine

import com.unicorn.api.application.hospital.HospitalDto
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import java.util.*

interface MedicineQueryService {
    fun getMedicines(): MedicineResult
}

@Service
class MedicineQueryServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : MedicineQueryService {

    override fun getMedicines(): MedicineResult {
        val sql = """
            SELECT
                medicine_id,
                medicine_name,
                count,
                quantity
            FROM medicines
            WHERE deleted_at IS NULL
        """.trimIndent()

        val medicines = namedParameterJdbcTemplate.query(
            sql,
            { rs, _ ->
                MedicineDto(
                    medicineID = rs.getObject("medicine_id", UUID::class.java),
                    medicineName = rs.getString("medicine_name"),
                    count = rs.getInt("count"),
                    quantity = rs.getInt("quantity")
                )
            }
        )

        return MedicineResult(medicines)
    }
}

data class MedicineResult(
    val data: List<MedicineDto>
)

data class MedicineDto(
    val medicineID: UUID,
    val medicineName: String,
    val count: Int,
    val quantity: Int
)