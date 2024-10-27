package com.unicorn.api.infrastructure.medicine

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import com.unicorn.api.domain.medicine.Medicine
import com.unicorn.api.domain.medicine.MedicineID
import java.util.*

interface MedicineRepository {
    fun store(medicine: Medicine): Medicine
    fun getOrNullBy(medicineID: MedicineID): Medicine?
    fun delete(medicine: Medicine): Unit
}

@Repository
class MedicineRepositoryImpl(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : MedicineRepository {

    override fun store(medicine: Medicine): Medicine {
        // language=postgresql
        val sql = """
            INSERT INTO medicines (
                medicine_id,
                user_id,
                medicine_name,
                count,
                quantity,
                created_at
            ) VALUES (
                :medicineID,
                :userID,
                :medicineName,
                :count,
                :quantity,
                NOW()
            ) 
            ON CONFLICT (medicine_id) 
            DO UPDATE SET
                medicine_name = EXCLUDED.medicine_name,
                count = EXCLUDED.count,
                quantity = EXCLUDED.quantity,
                deleted_at = NULL
            WHERE medicines.created_at IS NOT NULL;
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("medicineID", medicine.medicineID.value)
            .addValue("userID", medicine.userID.value)
            .addValue("medicineName", medicine.medicineName.value)
            .addValue("count", medicine.count.value)
            .addValue("quantity", medicine.quantity.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
        return medicine
    }

    override fun getOrNullBy(medicineID: MedicineID): Medicine? {
        // language=postgresql
        val sql = """
            SELECT
                medicine_id,
                user_id,
                medicine_name,
                count,
                quantity
            FROM medicines
            WHERE medicine_id = :medicineID
                AND deleted_at IS NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("medicineID", medicineID.value)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams
        ) { rs, _ ->
            Medicine.fromStore(
                medicineID = rs.getObject("medicine_id", UUID::class.java),
                medicineName = rs.getString("medicine_name"),
                userID = rs.getString("user_id"),
                count = rs.getInt("count"),
                quantity = rs.getInt("quantity")
            )
        }.singleOrNull()
    }

    override fun delete(medicine: Medicine) {
        // language=postgresql
        val sql = """
            UPDATE medicines
            SET deleted_at = NOW()
            WHERE medicine_id = :medicineID
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("medicineID", medicine.medicineID.value)

        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
