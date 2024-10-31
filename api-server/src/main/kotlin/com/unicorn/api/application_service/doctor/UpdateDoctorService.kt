package com.unicorn.api.application_service.doctor

import com.unicorn.api.controller.doctor.DoctorPutRequest
import com.unicorn.api.domain.call_support.CallSupportEndHour
import com.unicorn.api.domain.call_support.CallSupportStartHour
import com.unicorn.api.domain.chat_support.ChatSupportEndHour
import com.unicorn.api.domain.chat_support.ChatSupportStartHour
import com.unicorn.api.domain.department.DepartmentID
import com.unicorn.api.domain.doctor.*
import com.unicorn.api.domain.hospital.HospitalID
import com.unicorn.api.infrastructure.call_support.ChatSupportRepository
import com.unicorn.api.infrastructure.chat_support.CallSupportRepository
import com.unicorn.api.infrastructure.department.DepartmentRepository
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.hospital.HospitalRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UpdateDoctorService {
    fun update(
        doctorID: DoctorID,
        doctorPutRequest: DoctorPutRequest,
    ): DoctorPutRequest
}

@Service
class UpdateDoctorServiceImpl(
    private val doctorRepository: DoctorRepository,
    private val hospitalRepository: HospitalRepository,
    private val departmentRepository: DepartmentRepository,
    private val callSupportRepository: CallSupportRepository,
    private val chatSupportRepository: ChatSupportRepository,
) : UpdateDoctorService {
    @Transactional
    override fun update(
        doctorID: DoctorID,
        doctorPutRequest: DoctorPutRequest,
    ): DoctorPutRequest {
        val doctor = doctorRepository.getOrNullBy(doctorID)
        requireNotNull(doctor) { "Doctor not found" }

        // DB不整合チェック
        val chatSupport = chatSupportRepository.getOrNullBy(doctor.doctorID)
        requireNotNull(chatSupport) { "ChatSupport not found" }
        val callSupport = callSupportRepository.getOrNullBy(doctor.doctorID)
        requireNotNull(callSupport) { "CallSupport not found" }

        val hospital = hospitalRepository.getOrNullBy(HospitalID(doctorPutRequest.hospitalID))
        requireNotNull(hospital) { "Hospital not found" }

        val departmentIDs = doctorPutRequest.departments.map { DepartmentID(it) }
        val departments = departmentRepository.findByDepartmentIDs(departmentIDs)
        val missingDepartmentIDs = departmentIDs - departments.map { it?.departmentID }.toSet()
        // 　存在しないdepartmentIDがある場合はエラー
        require(missingDepartmentIDs.isEmpty()) {
            val response =
                missingDepartmentIDs.joinToString(
                    separator = ", ",
                    transform = { it?.value.toString() },
                )
            "Department not found: $response"
        }

        val updatedDoctor =
            doctor.update(
                hospitalID = hospital.hospitalID,
                email = Email(doctorPutRequest.email),
                phoneNumber = PhoneNumber(doctorPutRequest.phoneNumber),
                firstName = FirstName(doctorPutRequest.firstName),
                lastName = LastName(doctorPutRequest.lastName),
                doctorIconUrl = doctorPutRequest.doctorIconUrl?.let { DoctorIconUrl(it) },
                departments = departmentIDs,
            )
        val updatedChatSupport =
            chatSupport.update(
                chatSupportStartHour = ChatSupportStartHour(doctorPutRequest.chatSupportStartHour),
                chatSupportEndHour = ChatSupportEndHour(doctorPutRequest.chatSupportEndHour),
            )
        val updatedCallSupport =
            callSupport.update(
                callSupportStartHour = CallSupportStartHour(doctorPutRequest.callSupportStartHour),
                callSupportEndHour = CallSupportEndHour(doctorPutRequest.callSupportEndHour),
            )

        doctorRepository.store(updatedDoctor)
        chatSupportRepository.store(updatedChatSupport)
        callSupportRepository.store(updatedCallSupport)

        return doctorPutRequest
    }
}
