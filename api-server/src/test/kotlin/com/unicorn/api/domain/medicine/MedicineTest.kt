package com.unicorn.api.domain.medicine

import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID

class MedicineTest {
    @Test
    fun `should create medicine`() {
        val userID = UserID("test")
        val medicine =
            Medicine.create(
                medicineName = "Paracetamol",
                userID = userID,
                count = 10,
                quantity = 10,
                dosage = 5,
            )

        assertEquals(medicine.medicineID.value.toString(), medicine.medicineID.value.toString())
        assertEquals("Paracetamol", medicine.medicineName.value)
        assertEquals(10, medicine.count.value)
    }

    @Test
    fun `should create medicine from store`() {
        val medicineID = UUID.randomUUID()
        val medicine =
            Medicine.fromStore(
                medicineID = medicineID,
                medicineName = "Ibuprofen",
                userID = "test",
                count = 15,
                quantity = 30,
                dosage = 5,
            )

        assertEquals(medicine.medicineID.value.toString(), medicine.medicineID.value.toString())
        assertEquals("Ibuprofen", medicine.medicineName.value)
        assertEquals(15, medicine.count.value)
        assertEquals(30, medicine.quantity.value)
    }

    @Test
    fun `should throw InvalidMedicineIDException for invalid ID`() {
        val exception =
            assertThrows(InvalidMedicineIDException::class.java) {
                MedicineID.fromString("invalid-uuid")
            }
        assertEquals("Invalid medicine ID: invalid-uuid", exception.message)
    }

    @Test
    fun `should not create medicine with blank name`() {
        val userID = UserID("test")
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Medicine.create(
                    medicineName = "",
                    userID = userID,
                    count = 5,
                    quantity = 5,
                    dosage = 5,
                )
            }
        assertEquals("medicine name should not be blank", exception.message)
    }

    @Test
    fun `should not create medicine with negative count`() {
        val userID = UserID("test")
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Medicine.create(
                    medicineName = "Aspirin",
                    userID = userID,
                    count = -1,
                    quantity = 5,
                    dosage = 5,
                )
            }
        assertEquals("count should be 0 or greater", exception.message)
    }

    @Test
    fun `should not create medicine with zero quantity`() {
        val userID = UserID("test")
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Medicine.create(
                    medicineName = "Aspirin",
                    userID = userID,
                    count = -5,
                    quantity = 0,
                    dosage = 5,
                )
            }
        assertEquals("count should be 0 or greater", exception.message)
    }

    @Test
    fun `should not update medicine with quantity is smaller than count`() {
        val userID = UserID("test")
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                val medicine =
                    Medicine.create(
                        medicineName = "Aspirin",
                        userID = userID,
                        count = 20,
                        quantity = 10,
                        dosage = 5,
                    )

                medicine.update(
                    medicineName = MedicineName("Amoxicillin Extended"),
                    quantity = Quantity(50),
                    count = Count(20),
                    dosage = Dosage(5),
                )
            }
        assertEquals("quantity should be smaller than count.", exception.message)
    }

    @Test
    fun `should update medicine information`() {
        val userID = UserID("test")
        val medicine =
            Medicine.create(
                medicineName = "Amoxicillin",
                userID = userID,
                count = 20,
                quantity = 20,
                dosage = 5,
            )

        val updatedMedicine =
            medicine.update(
                medicineName = MedicineName("Amoxicillin Extended"),
                quantity = Quantity(18),
                count = Count(20),
                dosage = Dosage(5),
            )

        assertEquals("Amoxicillin Extended", updatedMedicine.medicineName.value)
        assertEquals(20, medicine.count.value)
        assertEquals(18, updatedMedicine.quantity.value)
    }
}
