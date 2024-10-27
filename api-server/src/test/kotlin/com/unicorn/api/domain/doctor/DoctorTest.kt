package com.unicorn.api.domain.doctor

import com.unicorn.api.domain.department.DepartmentID
import com.unicorn.api.domain.hospital.HospitalID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalTime
import java.util.*

class DoctorTest {

    @Test
    fun `create doctor`() {
        val doctorID = "doctorID"
        val hospitalID = HospitalID(UUID.randomUUID())
        val firstName = "firstName"
        val lastName = "lastName"
        val email = "email@test.com"
        val phoneNumber = "1234567890"
        val doctorIconUrl = "doctorIconUrl"
        val departments = listOf(DepartmentID(UUID.randomUUID()))

        val doctor = Doctor.create(
            doctorID,
            hospitalID,
            firstName,
            lastName,
            email,
            phoneNumber,
            doctorIconUrl,
            departments,
//            chatSupportStartHour,
//            chatSupportEndHour,
//            callSupportStartHour,
//            callSupportEndHour
        )

        assertThat(doctor.doctorID).isEqualTo(DoctorID(doctorID))
        assertThat(doctor.hospitalID).isEqualTo(hospitalID)
        assertThat(doctor.firstName).isEqualTo(FirstName(firstName))
        assertThat(doctor.lastName).isEqualTo(LastName(lastName))
        assertThat(doctor.email).isEqualTo(Email(email))
        assertThat(doctor.phoneNumber).isEqualTo(PhoneNumber(phoneNumber))
        assertThat(doctor.doctorIconUrl).isEqualTo(DoctorIconUrl(doctorIconUrl))
        assertThat(doctor.departments).isEqualTo(departments)
//        assertThat(doctor.chatSupportStartHour).isEqualTo(ChatSupportStartHour(chatSupportStartHour))
//        assertThat(doctor.chatSupportEndHour).isEqualTo(ChatSupportEndHour(chatSupportEndHour))
//        assertThat(doctor.callSupportStartHour).isEqualTo(CallSupportStartHour(callSupportStartHour))
//        assertThat(doctor.callSupportEndHour).isEqualTo(CallSupportEndHour(callSupportEndHour))
    }

    @Test
    fun `should update doctor`() {
        val doctorID = "doctorID"
        val hospitalID = HospitalID(UUID.randomUUID())
        val firstName = "firstName"
        val lastName = "lastName"
        val email = "email@test.com"
        val phoneNumber = "1234567890"
        val doctorIconUrl = "doctorIconUrl"
        val departments = listOf(DepartmentID(UUID.randomUUID()))

        val doctor = Doctor.create(
            doctorID,
            hospitalID,
            firstName,
            lastName,
            email,
            phoneNumber,
            doctorIconUrl,
            departments
        )

        val updatedFirstName = "updatedFirstName"

        val updateDoctor = doctor.update(
            hospitalID,
            FirstName(updatedFirstName),
            LastName(lastName),
            Email(email),
            PhoneNumber(phoneNumber),
            DoctorIconUrl(doctorIconUrl),
            departments
        )

        assertThat(updateDoctor.firstName).isEqualTo(FirstName(updatedFirstName))
    }

    @Test
    fun `should not update doctor with invalid email`() {
        val doctorID = "doctorID"
        val hospitalID = HospitalID(UUID.randomUUID())
        val firstName = "firstName"
        val lastName = "lastName"
        val email = "invalid-email"
        val phoneNumber = "1234567890"
        val doctorIconUrl = "doctorIconUrl"
        val departments = listOf(DepartmentID(UUID.randomUUID()))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            Doctor.create(
                doctorID,
                hospitalID,
                firstName,
                lastName,
                email,
                phoneNumber,
                doctorIconUrl,
                departments
            )
        }

        assertThat(exception.message).isEqualTo("email should be valid")
    }
}