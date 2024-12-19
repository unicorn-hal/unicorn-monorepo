package com.unicorn.api.application_service.doctor_department

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.infrastructure.department.DepartmentRepository
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import org.springframework.stereotype.Service

interface DeleteDoctorDepartmentsService {
    fun deleteByDoctorID(doctorID: DoctorID)
}

@Service
class DeleteDoctorDepartmentsServiceImpl(
    private val doctorRepository: DoctorRepository,
    private val departmentRepository: DepartmentRepository,
) : DeleteDoctorDepartmentsService {
    override fun deleteByDoctorID(doctorID: DoctorID) {
        val doctor = doctorRepository.getOrNullBy(doctorID)
        requireNotNull(doctor) { "Doctor not found" }

        departmentRepository.deleteByDoctor(doctor)
    }
}
