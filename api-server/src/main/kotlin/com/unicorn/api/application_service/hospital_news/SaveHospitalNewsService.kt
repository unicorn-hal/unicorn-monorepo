package com.unicorn.api.application_service.hospital_news

import com.unicorn.api.controller.hospital_news.HospitalNewsRequest
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.hospital.HospitalID
import com.unicorn.api.domain.hospital_news.HospitalNews
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.hospital.HospitalRepository
import com.unicorn.api.infrastructure.hospital_news.HospitalNewsRepository
import org.springframework.stereotype.Service

interface SaveHospitalNewsService {
    fun save(
        doctorID: DoctorID,
        hospitalID: HospitalID,
        hospitalNewsRequest: HospitalNewsRequest,
    ): HospitalNews
}

@Service
class SaveHospitalNewsImpl(
    private val doctorRepository: DoctorRepository,
    private val hospitalRepository: HospitalRepository,
    private val hospitalNewsRepository: HospitalNewsRepository,
) : SaveHospitalNewsService {
    override fun save(
        doctorID: DoctorID,
        hospitalID: HospitalID,
        hospitalNewsRequest: HospitalNewsRequest,
    ): HospitalNews {
        val doctor = doctorRepository.getOrNullBy(doctorID)
        requireNotNull(doctor) { "Doctor not found" }

        val hospital = hospitalRepository.getOrNullBy(hospitalID)
        requireNotNull(hospital) { "Hospital not found" }

        val hospitalNews =
            HospitalNews.create(
                hospitalID = hospitalID.value,
                title = hospitalNewsRequest.title,
                contents = hospitalNewsRequest.contents,
                noticeImageUrl = hospitalNewsRequest.noticeImageUrl,
                relatedUrl = hospitalNewsRequest.relatedUrl,
            )

        hospitalNewsRepository.store(hospitalNews)

        return hospitalNews
    }
}
