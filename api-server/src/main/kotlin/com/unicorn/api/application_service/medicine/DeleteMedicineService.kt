package com.unicorn.api.application_service.medicine

import com.unicorn.api.domain.medicine.MedicineID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.medicine.MedicineRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface DeleteMedicineService {
    fun delete(userID: UserID, medicineID: MedicineID): Unit
}

@Service
class DeleteMedicineServiceImpl(
    private val userRepository: UserRepository,
    private val medicineRepository: MedicineRepository
) : DeleteMedicineService {
    override fun delete(userID: UserID, medicineID: MedicineID) {
        val user = userRepository.getOrNullBy(userID)

        requireNotNull(user) {
            "User not found"
        }

        val medicine = medicineRepository.getOrNullBy(medicineID)

        requireNotNull(medicine) {
            "Medicine not found"
        }

        medicineRepository.delete(medicine)
    }
}