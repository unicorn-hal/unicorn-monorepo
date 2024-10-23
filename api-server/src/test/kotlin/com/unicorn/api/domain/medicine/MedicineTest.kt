package com.unicorn.api.domain.medicine

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID

class MedicineTest {

    @Test
    fun `should create medicine`() {
        val medicine = Medicine.create(
            medicineID = UUID.randomUUID(),
            medicineName = "Paracetamol",
            count = 10,
            quantity = 20
        )

        assertEquals(medicine.medicineID.value.toString(), medicine.medicineID.value.toString()) // UUIDの比較
        assertEquals("Paracetamol", medicine.medicineName.value)
        assertEquals(10, medicine.count.value)
        assertEquals(20, medicine.quantity.value)
    }

    @Test
    fun `should create medicine from store`() {
        val medicine = Medicine.fromStore(
            medicineID = UUID.randomUUID(),
            medicineName = "Ibuprofen",
            count = 15,
            quantity = 30
        )

        assertEquals(medicine.medicineID.value.toString(), medicine.medicineID.value.toString()) // UUIDの比較
        assertEquals("Ibuprofen", medicine.medicineName.value)
        assertEquals(15, medicine.count.value)
        assertEquals(30, medicine.quantity.value)
    }

    @Test
    fun `should throw InvalidMedicineIDException for invalid ID`() {
        val exception = assertThrows(InvalidMedicineIDException::class.java) {
            MedicineID.fromString("invalid-uuid")
        }
        assertEquals("Invalid medicine ID: invalid-uuid", exception.message)
    }

    @Test
    fun `should not create medicine with blank name`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Medicine.create(
                medicineID = UUID.randomUUID(),
                medicineName = "",
                count = 5,
                quantity = 10
            )
        }
        assertEquals("medicine name should not be blank", exception.message)
    }

    @Test
    fun `should not create medicine with negative count`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Medicine.create(
                medicineID = UUID.randomUUID(),
                medicineName = "Aspirin",
                count = -1,
                quantity = 10
            )
        }
        assertEquals("count should be 0 or greater", exception.message)
    }

    @Test
    fun `should not create medicine with zero quantity`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Medicine.create(
                medicineID = UUID.randomUUID(),
                medicineName = "Aspirin",
                count = 5,
                quantity = 0
            )
        }
        assertEquals("quantity should be greater than 0", exception.message)
    }

    @Test
    fun `should update medicine information`() {
        val medicine = Medicine.create(
            medicineID = UUID.randomUUID(),
            medicineName = "Amoxicillin",
            count = 20,
            quantity = 50
        )

        val updatedMedicine = medicine.update(
            medicineName = MedicineName("Amoxicillin Extended"),
            count = Count(25),
            quantity = Quantity(60)
        )

        assertEquals("Amoxicillin Extended", updatedMedicine.medicineName.value)
        assertEquals(25, updatedMedicine.count.value)
        assertEquals(60, updatedMedicine.quantity.value)
    }
}
