package com.unicorn.api.application_service.doctor

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.infrastructure.call_support.ChatSupportRepository
import com.unicorn.api.infrastructure.chat_support.CallSupportRepository
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface DoctorDeleteService {
    fun delete(doctorID: DoctorID): Unit
}

@Service
class DoctorDeleteServiceImpl (
    private val doctorRepository: DoctorRepository,
    private val callSupportRepository: CallSupportRepository,
    private val chatSupportRepository: ChatSupportRepository
): DoctorDeleteService {
    @Transactional
    override fun delete(doctorID: DoctorID) {
        val doctor = doctorRepository.getOrNullBy(doctorID)
        requireNotNull(doctor) { "Doctor not found" }

        // DB不整合チェック
        val chatSupport = chatSupportRepository.getOrNullBy(doctor.doctorID)
        requireNotNull(chatSupport) { "ChatSupport not found" }
        val callSupport = callSupportRepository.getOrNullBy(doctor.doctorID)
        requireNotNull(callSupport) { "CallSupport not found" }

        doctorRepository.delete(doctor)
        chatSupportRepository.delete(chatSupport)
        callSupportRepository.delete(callSupport)
    }
}