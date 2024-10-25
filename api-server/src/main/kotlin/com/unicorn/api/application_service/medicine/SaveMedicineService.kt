package com.unicorn.api.application_service.medicine

import com.unicorn.api.controller.medicine.MedicinePostData
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.medicine.Medicine
import com.unicorn.api.domain.medicine.MedicineID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.account.AccountRepository
import com.unicorn.api.infrastructure.medicine.MedicineRepository
import org.springframework.stereotype.Service
import java.util.*

interface SaveMedicineService {
    fun save(uid: UID, medicinePostData: MedicinePostData): Medicine
}

@Service
class SaveMedicineServiceImpl(
    private val accountRepository: AccountRepository,
    private val medicineRepository: MedicineRepository
) : SaveMedicineService {
    override fun save(uid: UID, medicinePostData: MedicinePostData): Medicine {
        val account = accountRepository.getOrNullByUid(uid)
        requireNotNull(account) { "Account not found" }

        val existingMedicine = medicineRepository.getOrNullBy(MedicineID(medicinePostData.medicineID))
        require(existingMedicine == null) { "Medicine already exists" }

        val medicine = Medicine.create(
            medicineID = medicinePostData.medicineID,
            medicineName = medicinePostData.medicineName,
            count = medicinePostData.count,
            quantity = medicinePostData.quantity
        )

        medicineRepository.store(medicine, uid)

        return medicine
    }
}
