package com.unicorn.api.application_service.medicine

import com.unicorn.api.controller.medicine.MedicinePutRequest
import com.unicorn.api.domain.medicine.Medicine
import com.unicorn.api.domain.medicine.MedicineID
import com.unicorn.api.domain.medicine.MedicineName
import com.unicorn.api.domain.medicine.Quantity
import com.unicorn.api.domain.user.*
import com.unicorn.api.infrastructure.medicine.MedicineRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface UpdateMedicineService {
    fun update(
        medicineID: MedicineID,
        userID: UserID,
        medicinePutRequest: MedicinePutRequest,
    ): Medicine
}

@Service
class UpdateMedicineServiceImpl(
    private val userRepository: UserRepository,
    private val medicineRepository: MedicineRepository,
) : UpdateMedicineService {
    override fun update(
        medicineID: MedicineID,
        userID: UserID,
        medicinePutRequest: MedicinePutRequest,
    ): Medicine {
        val user = userRepository.getOrNullBy(userID)

        requireNotNull(user) {
            "User not found"
        }

        val medicine = medicineRepository.getOrNullBy(medicineID)

        requireNotNull(medicine) {
            "Medicine not found"
        }

        val updateMedicine =
            medicine.update(
                medicineName = MedicineName(medicinePutRequest.medicineName),
                quantity = Quantity(medicinePutRequest.quantity),
            )

        medicineRepository.store(updateMedicine)

        return updateMedicine
    }
}
