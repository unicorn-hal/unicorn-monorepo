package com.unicorn.api.application_service.medicine

import com.unicorn.api.controller.medicine.MedicinePostRequest
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.medicine.Medicine
import com.unicorn.api.domain.medicine_reminders.MedicineReminder
import com.unicorn.api.domain.medicine_reminders.MedicineReminders
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.medicine.MedicineRepository
import com.unicorn.api.infrastructure.medicine_reminders.MedicineRemindersRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface SaveMedicineService {
    fun save(
        uid: UID,
        medicinePostRequest: MedicinePostRequest,
    ): Medicine
}

@Service
class SaveMedicineServiceImpl(
    private val userRepository: UserRepository,
    private val medicineRepository: MedicineRepository,
    private val medicineRemindersRepository: MedicineRemindersRepository,
) : SaveMedicineService {
    @Transactional
    override fun save(
        uid: UID,
        medicinePostRequest: MedicinePostRequest,
    ): Medicine {
        val user = userRepository.getOrNullBy(UserID(uid.value))
        requireNotNull(user) { "User not found" }

        val medicine =
            Medicine.create(
                medicineName = medicinePostRequest.medicineName,
                userID = user.userID,
                count = medicinePostRequest.count,
                quantity = medicinePostRequest.quantity,
                dosage = medicinePostRequest.dosage,
            )

        val medicineReminders =
            MedicineReminders.of(
                medicineID = medicine.medicineID,
                reminders =
                    medicinePostRequest.reminders.map { reminder ->
                        MedicineReminder.of(
                            reminderID = reminder.reminderID,
                            reminderTime = reminder.reminderTime,
                            dayOfWeek = reminder.reminderDayOfWeek,
                        )
                    },
            )

        medicineRepository.store(medicine)
        medicineRemindersRepository.store(medicineReminders)

        return medicine
    }
}
