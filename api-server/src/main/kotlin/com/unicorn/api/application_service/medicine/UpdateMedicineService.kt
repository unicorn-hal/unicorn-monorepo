package com.unicorn.api.application_service.medicine

import com.unicorn.api.controller.medicine.MedicinePutRequest
import com.unicorn.api.domain.medicine.*
import com.unicorn.api.domain.medicine_reminders.MedicineReminder
import com.unicorn.api.domain.user.*
import com.unicorn.api.infrastructure.medicine.MedicineRepository
import com.unicorn.api.infrastructure.medicine_reminders.MedicineRemindersRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UpdateMedicineService {
    fun update(
        medicineID: MedicineID,
        userID: UserID,
        medicinePutRequest: MedicinePutRequest,
    ): MedicinePutRequest
}

@Service
class UpdateMedicineServiceImpl(
    private val userRepository: UserRepository,
    private val medicineRepository: MedicineRepository,
    private val medicineRemindersRepository: MedicineRemindersRepository,
) : UpdateMedicineService {
    @Transactional
    override fun update(
        medicineID: MedicineID,
        userID: UserID,
        medicinePutRequest: MedicinePutRequest,
    ): MedicinePutRequest {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) {
            "User not found"
        }

        val medicine = medicineRepository.getOrNullBy(medicineID)
        requireNotNull(medicine) {
            "Medicine not found"
        }

        val medicineReminders = medicineRemindersRepository.getBy(medicineID)

        val updateMedicine =
            medicine.update(
                medicineName = MedicineName(medicinePutRequest.medicineName),
                quantity = Quantity(medicinePutRequest.quantity),
                count = Count(medicinePutRequest.count),
                dosage = Dosage(medicinePutRequest.dosage),
            )

        val updateMedicineReminders =
            medicineReminders.update(
                reminders =
                    medicinePutRequest.reminders.map { reminder ->
                        MedicineReminder.of(
                            reminderID = reminder.reminderID,
                            reminderTime = reminder.reminderTime,
                            dayOfWeek = reminder.reminderDayOfWeek,
                        )
                    },
            )

        medicineRepository.store(updateMedicine)
        medicineRemindersRepository.store(updateMedicineReminders)

        return medicinePutRequest
    }
}
