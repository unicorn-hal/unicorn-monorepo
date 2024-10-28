package com.unicorn.api.application_service.doctor

import com.unicorn.api.controller.doctor.DoctorPostRequest
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.call_support.CallSupport
import com.unicorn.api.domain.chat_support.ChatSupport
import com.unicorn.api.domain.department.DepartmentID
import com.unicorn.api.domain.doctor.Doctor
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.hospital.HospitalID
import com.unicorn.api.infrastructure.account.AccountRepository
import com.unicorn.api.infrastructure.call_support.ChatSupportRepository
import com.unicorn.api.infrastructure.chat_support.CallSupportRepository
import com.unicorn.api.infrastructure.department.DepartmentRepository
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.hospital.HospitalRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface DoctorSaveService {
    fun save(uid: UID, doctorPostRequest: DoctorPostRequest): DoctorPostRequest
}

@Service
class DoctorSaveServiceImpl(
    private val accountRepository: AccountRepository,
    private val doctorRepository: DoctorRepository,
    private val hospitalRepository: HospitalRepository,
    private val departmentRepository: DepartmentRepository,
    private val callSupportRepository: CallSupportRepository,
    private val chatSupportRepository: ChatSupportRepository
) : DoctorSaveService {
    @Transactional
    override fun save(uid: UID, doctorPostRequest: DoctorPostRequest): DoctorPostRequest {
        val account = accountRepository.getOrNullByUid(uid)
        requireNotNull(account) { "Account not found" }
        require(account.isDoctor()) { "Account is not doctor" }

        val existingDoctor = doctorRepository.getOrNullBy(DoctorID(account.uid.value))
        require(existingDoctor == null) { "Doctor already exists" }

        // DB不整合チェック
        val existingCallSupport = callSupportRepository.getOrNullBy(DoctorID(account.uid.value))
        require(existingCallSupport == null) { "CallSupport already exists" }
        val existingChatSupport = chatSupportRepository.getOrNullBy(DoctorID(account.uid.value))
        require(existingChatSupport == null) { "ChatSupport already exists" }

        val hospital = hospitalRepository.getOrNullBy(HospitalID(doctorPostRequest.hospitalID))
        requireNotNull(hospital) { "Hospital not found" }

        val departmentIDs = doctorPostRequest.departments.map { DepartmentID(it) }
        val departments = departmentRepository.findByDepartmentIDs(departmentIDs)
        val missingDepartmentIDs = departmentIDs - departments.map { it?.departmentID }.toSet()
        // 存在しないdepartmentIDがある場合はエラー
        require(missingDepartmentIDs.isEmpty()) {
            val response = missingDepartmentIDs.joinToString(
                separator = ", ",
                transform = { it?.value.toString() }
            )
            "Department not found: $response"
        }

        val doctor = Doctor.create(
            doctorID = uid.value,
            hospitalID = hospital.hospitalID,
            firstName = doctorPostRequest.firstName,
            lastName = doctorPostRequest.lastName,
            email = doctorPostRequest.email,
            phoneNumber = doctorPostRequest.phoneNumber,
            doctorIconUrl = doctorPostRequest.doctorIconUrl,
            departments = departmentIDs,
        )
        val callSupport = CallSupport.create(
            doctorID = doctor.doctorID,
            callSupportStartHour = doctorPostRequest.callSupportStartHour,
            callSupportEndHour = doctorPostRequest.callSupportEndHour,
        )
        val chatSupport = ChatSupport.create(
            doctorID = doctor.doctorID,
            chatSupportStartHour = doctorPostRequest.chatSupportStartHour,
            chatSupportEndHour = doctorPostRequest.chatSupportEndHour,
        )

        doctorRepository.store(doctor)
        callSupportRepository.store(callSupport)
        chatSupportRepository.store(chatSupport)

        return doctorPostRequest
    }
}