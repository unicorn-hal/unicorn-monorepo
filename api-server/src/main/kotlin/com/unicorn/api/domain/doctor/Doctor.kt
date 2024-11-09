package com.unicorn.api.domain.doctor

import com.unicorn.api.domain.department.DepartmentID
import com.unicorn.api.domain.hospital.HospitalID
import java.util.*

data class Doctor private constructor(
    val doctorID: DoctorID,
    val hospitalID: HospitalID,
    val firstName: FirstName,
    val lastName: LastName,
    val email: Email,
    val phoneNumber: PhoneNumber,
    val doctorIconUrl: DoctorIconUrl?,
    val departments: List<DepartmentID>,
) {
    companion object {
        fun create(
            doctorID: String,
            hospitalID: HospitalID,
            firstName: String,
            lastName: String,
            email: String,
            phoneNumber: String,
            doctorIconUrl: String?,
            departments: List<DepartmentID>,
        ): Doctor {
            require(departments.toSet().size == departments.size) { "departmentID must be unique" }

            return Doctor(
                doctorID = DoctorID(doctorID),
                hospitalID = hospitalID,
                firstName = FirstName(firstName),
                lastName = LastName(lastName),
                email = Email(email),
                phoneNumber = PhoneNumber(phoneNumber),
                doctorIconUrl = doctorIconUrl?.let { DoctorIconUrl(it) },
                departments = departments,
            )
        }

        fun fromStore(
            doctorID: String,
            hospitalID: UUID,
            firstName: String,
            lastName: String,
            email: String,
            phoneNumber: String,
            doctorIconUrl: String?,
            departments: List<UUID>,
        ): Doctor {
            return Doctor(
                doctorID = DoctorID(doctorID),
                hospitalID = HospitalID(hospitalID),
                firstName = FirstName(firstName),
                lastName = LastName(lastName),
                email = Email(email),
                phoneNumber = PhoneNumber(phoneNumber),
                doctorIconUrl = doctorIconUrl?.let { DoctorIconUrl(it) },
                departments = departments.map { DepartmentID(it) },
            )
        }
    }

    fun update(
        hospitalID: HospitalID,
        firstName: FirstName,
        lastName: LastName,
        email: Email,
        phoneNumber: PhoneNumber,
        doctorIconUrl: DoctorIconUrl?,
        departments: List<DepartmentID>,
    ): Doctor {
        return this.copy(
            hospitalID = hospitalID,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phoneNumber = phoneNumber,
            doctorIconUrl = doctorIconUrl,
            departments = departments,
        )
    }
}

@JvmInline
value class DoctorID(val value: String) {
    init {
        require(value.isNotBlank()) { "DoctorID must not be blank" }
    }
}

@JvmInline
value class FirstName(val value: String) {
    init {
        require(value.isNotBlank()) { "first name should not be blank" }
    }
}

@JvmInline
value class LastName(val value: String) {
    init {
        require(value.isNotBlank()) { "last name should not be blank" }
    }
}

@JvmInline
value class Email(val value: String) {
    init {
        require(value.contains("@")) { "email should be valid" }
    }
}

@JvmInline
value class PhoneNumber(val value: String) {
    init {
        require(value.all { it.isDigit() }) { "phone number should be all digits" }
    }
}

@JvmInline
value class DoctorIconUrl(val value: String)
