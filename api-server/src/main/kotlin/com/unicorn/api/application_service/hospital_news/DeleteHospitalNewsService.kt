package com.unicorn.api.application_service.hospital_news

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.hospital.HospitalID
import com.unicorn.api.domain.hospital_news.HospitalNewsID
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.hospital.HospitalRepository
import com.unicorn.api.infrastructure.hospital_news.HospitalNewsRepository
import org.springframework.stereotype.Service

interface DeleteHospitalNewsService {
    fun delete(
        doctorID: DoctorID,
        hospitalID: HospitalID,
        hospitalNewsID: HospitalNewsID,
    )
}

@Service
class DeleteHospitalNewsServiceImpl(
    private val doctorRepository: DoctorRepository,
    private val hospitalRepository: HospitalRepository,
    private val hospitalNewsRepository: HospitalNewsRepository,
) : DeleteHospitalNewsService {
    override fun delete(
        doctorID: DoctorID,
        hospitalID: HospitalID,
        hospitalNewsID: HospitalNewsID,
    ) {
        val doctor = doctorRepository.getOrNullBy(doctorID)
        requireNotNull(doctor) { "Doctor not found" }

        val hospital = hospitalRepository.getOrNullBy(hospitalID)
        requireNotNull(hospital) { "Hospital not found" }

        val hospitalNews = hospitalNewsRepository.getOrNullBy(hospitalNewsID)
        requireNotNull(hospitalNews) { "Hospital News not found" }

        hospitalNewsRepository.delete(hospitalNews)
    }
}
