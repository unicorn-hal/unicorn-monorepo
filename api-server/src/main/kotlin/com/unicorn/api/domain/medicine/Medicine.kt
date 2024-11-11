package com.unicorn.api.domain.medicine

import com.unicorn.api.domain.user.*
import java.util.*

data class Medicine private constructor(
    val medicineID: MedicineID,
    val medicineName: MedicineName,
    val userID: UserID,
    val count: Count,
    val quantity: Quantity,
    val dosage: Dosage,
) {
    companion object {
        fun fromStore(
            medicineID: UUID,
            medicineName: String,
            userID: String,
            count: Int,
            quantity: Int,
            dosage: Int,
        ): Medicine {
            return Medicine(
                medicineID = MedicineID(medicineID),
                medicineName = MedicineName(medicineName),
                userID = UserID(userID),
                count = Count(count),
                quantity = Quantity(quantity),
                dosage = Dosage(dosage),
            )
        }

        fun create(
            medicineName: String,
            userID: UserID,
            quantity: Int,
            count: Int,
            dosage: Int,
        ): Medicine {
            return Medicine(
                medicineID = MedicineID(UUID.randomUUID()),
                medicineName = MedicineName(medicineName),
                userID = userID,
                count = Count(count),
                quantity = Quantity(quantity),
                dosage = Dosage(dosage),
            )
        }
    }

    fun update(
        medicineName: MedicineName,
        quantity: Quantity,
        count: Count,
        dosage: Dosage,
    ): Medicine {
        require(quantity.value <= count.value) {
            "quantity should be smaller than count."
        }
        return this.copy(
            medicineName = medicineName,
            quantity = quantity,
            count = count,
            dosage = dosage,
        )
    }
}

class InvalidMedicineIDException(message: String) : Exception(message)

@JvmInline
value class MedicineID(val value: UUID) {
    init {
        require(value.toString().isNotBlank()) { "medicineID should not be blank" }
    }

    companion object {
        fun fromString(id: String): MedicineID {
            return try {
                MedicineID(UUID.fromString(id))
            } catch (e: IllegalArgumentException) {
                throw InvalidMedicineIDException("Invalid medicine ID: $id")
            }
        }
    }
}

@JvmInline
value class MedicineName(val value: String) {
    init {
        require(value.isNotBlank()) { "medicine name should not be blank" }
    }
}

@JvmInline
value class Count(val value: Int) {
    init {
        require(value >= 0) { "count should be 0 or greater" }
    }
}

@JvmInline
value class Quantity(val value: Int) {
    init {
        require(value > 0) { "quantity should be greater than 0" }
    }
}

@JvmInline
value class Dosage(val value: Int) {
    init {
        require(value > 0) { "dosage should be greater than 0" }
    }
}
