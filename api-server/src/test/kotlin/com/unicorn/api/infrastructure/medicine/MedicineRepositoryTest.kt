package com.unicorn.api.infrastructure.medicine

import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.medicine.Count
import com.unicorn.api.domain.medicine.Medicine
import com.unicorn.api.domain.medicine.MedicineID
import com.unicorn.api.domain.medicine.MedicineName
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
        val sql = """
            SELECT
                medicine_id,
                user_id,
                medicine_name,
                count,
                quantity
            FROM medicines WHERE medicine_id = :medicineID AND deleted_at IS NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource().addValue("medicineID", medicineID.value)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams
        ) { rs, _ ->
            Medicine.fromStore(
                medicineID = rs.getObject("medicine_id", UUID::class.java),
                medicineName = rs.getString("medicine_name"),
                count = rs.getInt("count"),
                quantity = rs.getInt("quantity")
            )
        }.singleOrNull()
    }

    @Test
    fun `should store medicine`() {
        val medicine = Medicine.create(
            medicineID = UUID.fromString("123e4567-e89b-12d3-a456-426614174004"),
            medicineName = "Aspirin",
            count = 10,
            quantity = 5
        )

        val userID = UID("test")
        medicineRepository.store(medicine, userID)

        val storedMedicine = findMedicineByID(medicine.medicineID)
        assert(storedMedicine?.medicineID == medicine.medicineID)
        assert(storedMedicine?.medicineName == medicine.medicineName)
        assert(storedMedicine?.count == medicine.count)
        assert(storedMedicine?.quantity == medicine.quantity)
    }

    @Test
    fun `should update medicine`() {
        val medicine = Medicine.create(
            medicineID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
            medicineName = "Ibuprofen",
            count = 20,
            quantity = 3
        )

        val userID = UID("test")
        medicineRepository.store(medicine, userID)

        // Update the medicine
        val updatedMedicine = medicine.copy(medicineName = MedicineName("Ibuprofen Extra"), count = Count(15))
        medicineRepository.store(updatedMedicine, userID)

        val result = findMedicineByID(updatedMedicine.medicineID)
        assert(result?.medicineID == updatedMedicine.medicineID)
        assert(result?.medicineName == updatedMedicine.medicineName)
        assert(result?.count == updatedMedicine.count)
        assert(result?.quantity == updatedMedicine.quantity)
    }

    @Test
    fun `should find medicine by ID`() {
        val medicineID = MedicineID.fromString("123e4567-e89b-12d3-a456-426614174002")

        val foundMedicine = medicineRepository.getOrNullBy(medicineID)

        assert(foundMedicine?.medicineID == medicineID)
        assert(foundMedicine?.medicineName?.value == "Aspirin")
        assert(foundMedicine?.count?.value == 8)
        assert(foundMedicine?.quantity?.value == 20)
    }

    @Test
    fun `should delete medicine`() {
        val medicineIDString = "123e4567-e89b-12d3-a456-426614174004"
        val medicineID = MedicineID.fromString(medicineIDString)
        val medicine = Medicine.create(
            medicineID = UUID.fromString(medicineIDString),
            medicineName = "Test Medicine",
            count = 1,
            quantity = 1
        )

        val userID = UID("test")
        medicineRepository.store(medicine, userID)

        // Delete the medicine
        medicineRepository.delete(medicine)

        val deletedMedicine = findMedicineByID(medicineID)
        assert(deletedMedicine == null)
    }
}
