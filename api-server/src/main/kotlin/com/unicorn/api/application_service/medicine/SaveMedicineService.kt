package com.unicorn.api.application_service.medicine

import com.unicorn.api.controller.medicine.MedicinePostRequest
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.medicine.Medicine
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.medicine.MedicineRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import java.util.*

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
) : SaveMedicineService {
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
            )

        medicineRepository.store(medicine)

        return medicine
    }
}
