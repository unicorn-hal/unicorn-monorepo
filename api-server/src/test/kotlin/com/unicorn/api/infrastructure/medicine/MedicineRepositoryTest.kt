package com.unicorn.api.infrastructure.medicine

import com.unicorn.api.domain.medicine.*
import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.test.assertEquals

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/medicine/Insert_Medicine_Data.sql")
class MedicineRepositoryTest {
    @Autowired
    private lateinit var medicineRepository: MedicineRepositoryImpl

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findMedicineByID(medicineID: MedicineID): Medicine? {
        val sql =
            """
            SELECT
                medicine_id,
                user_id,
                medicine_name,
                count,
                quantity,
                dosage
            FROM medicines WHERE medicine_id = :medicineID AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams = MapSqlParameterSource().addValue("medicineID", medicineID.value)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams,
        ) { rs, _ ->
            Medicine.fromStore(
                medicineID = rs.getObject("medicine_id", UUID::class.java),
                medicineName = rs.getString("medicine_name"),
                userID = rs.getString("user_id"),
                count = rs.getInt("count"),
                quantity = rs.getInt("quantity"),
                dosage = rs.getInt("dosage"),
            )
        }.singleOrNull()
    }

    @Test
    fun `should store medicine`() {
        val userID = UserID("test")
        val medicine =
            Medicine.create(
                medicineName = "Aspirin",
                userID = userID,
                count = 10,
                quantity = 10,
                dosage = 5,
            )

        medicineRepository.store(medicine)

        val storedMedicine = findMedicineByID(medicine.medicineID)
        assertEquals(medicine.medicineID, storedMedicine?.medicineID)
        assertEquals(medicine.medicineName, storedMedicine?.medicineName)
        assertEquals(medicine.count, storedMedicine?.count)
        assertEquals(medicine.quantity, storedMedicine?.quantity)
        assertEquals(medicine.dosage, storedMedicine?.dosage)
    }

    @Test
    fun `should update medicine`() {
        val userID = UserID("test")
        val medicine =
            Medicine.fromStore(
                medicineID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
                medicineName = "Ibuprofen",
                userID = userID.value,
                count = 30,
                quantity = 15,
                dosage = 5,
            )
        val updatedMedicine =
            medicine.update(
                medicineName = medicine.medicineName,
                quantity = Quantity(10),
                count = Count(30),
                dosage = Dosage(10),
            )

        medicineRepository.store(updatedMedicine)

        val result = findMedicineByID(medicine.medicineID)
        assertEquals(updatedMedicine.medicineID, result?.medicineID)
        assertEquals(updatedMedicine.medicineName, result?.medicineName)
        assertEquals(updatedMedicine.count, result?.count)
        assertEquals(updatedMedicine.quantity, result?.quantity)
        assertEquals(updatedMedicine.dosage, result?.dosage)
    }

    @Test
    fun `should find medicine by ID`() {
        val medicineID = MedicineID.fromString("123e4567-e89b-12d3-a456-426614174002")

        val foundMedicine = medicineRepository.getOrNullBy(medicineID)

        assertEquals(medicineID, foundMedicine?.medicineID)
        assertEquals("Aspirin", foundMedicine?.medicineName?.value)
        assertEquals(80, foundMedicine?.count?.value)
        assertEquals(20, foundMedicine?.quantity?.value)
        assertEquals(1, foundMedicine?.dosage?.value)
    }

    @Test
    fun `should delete medicine`() {
        val medicineIDString = "123e4567-e89b-12d3-a456-426614174004"
        val medicineID = MedicineID.fromString(medicineIDString)
        val userID = UserID("test")
        val medicine =
            Medicine.create(
                medicineName = "Test Medicine",
                userID = userID,
                count = 1,
                quantity = 1,
                dosage = 1,
            )

        medicineRepository.store(medicine)

        // Delete the medicine
        medicineRepository.delete(medicine)

        val deletedMedicine = findMedicineByID(medicineID)
        assertEquals(null, deletedMedicine)
    }
}
