package com.unicorn.api.application_service.medicine

import com.unicorn.api.domain.medicine.MedicineID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.medicine.MedicineRepository
import com.unicorn.api.infrastructure.medicine_reminders.MedicineRemindersRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface DeleteMedicineService {
    fun delete(
        userID: UserID,
        medicineID: MedicineID,
    ): Unit

    fun deleteByUserID(userID: UserID)
}

@Service
class DeleteMedicineServiceImpl(
    private val userRepository: UserRepository,
    private val medicineRepository: MedicineRepository,
    private val medicineRemindersRepository: MedicineRemindersRepository,
) : DeleteMedicineService {
    @Transactional
    override fun delete(
        userID: UserID,
        medicineID: MedicineID,
    ) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) {
            "User not found"
        }

        val medicine = medicineRepository.getOrNullBy(medicineID)
        requireNotNull(medicine) {
            "Medicine not found"
        }

        val medicineReminders = medicineRemindersRepository.getBy(medicine.medicineID)

        medicineRepository.delete(medicine)
        medicineRemindersRepository.delete(medicineReminders)
    }

    override fun deleteByUserID(userID: UserID) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        medicineRepository.deleteByUser(user)

        medicineRemindersRepository.deleteByUser(user)
    }
}
