package com.unicorn.api.domain.doctor

import com.unicorn.api.domain.department.DepartmentID
import com.unicorn.api.domain.hospital.HospitalID
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

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

        val doctor =
            Doctor.create(
                doctorID,
                hospitalID,
                firstName,
                lastName,
                email,
                phoneNumber,
                doctorIconUrl,
                departments,
            )

        assertEquals(doctorID, doctor.doctorID.value)
        assertEquals(hospitalID, doctor.hospitalID)
        assertEquals(firstName, doctor.firstName.value)
        assertEquals(lastName, doctor.lastName.value)
        assertEquals(email, doctor.email.value)
        assertEquals(phoneNumber, doctor.phoneNumber.value)
        assertEquals(doctorIconUrl, doctor.doctorIconUrl?.value)
        assertEquals(departments, doctor.departments)
    }

    @Test
    fun `create doctor from store`() {
        val doctorID = "doctorID"
        val hospitalID = UUID.randomUUID()
        val firstName = "firstName"
        val lastName = "lastName"
        val email = "test@test.com"
        val phoneNumber = "1234567890"
        val doctorIconUrl = "doctorIconUrl"
        val departments = listOf(UUID.randomUUID())

        val doctor =
            Doctor.fromStore(
                doctorID,
                hospitalID,
                firstName,
                lastName,
                email,
                phoneNumber,
                doctorIconUrl,
                departments,
            )

        assertEquals(doctorID, doctor.doctorID.value)
        assertEquals(hospitalID, doctor.hospitalID.value)
        assertEquals(firstName, doctor.firstName.value)
        assertEquals(lastName, doctor.lastName.value)
        assertEquals(email, doctor.email.value)
        assertEquals(phoneNumber, doctor.phoneNumber.value)
        assertEquals(doctorIconUrl, doctor.doctorIconUrl?.value)
        assertEquals(departments, doctor.departments.map { it.value })
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

        val doctor =
            Doctor.create(
                doctorID,
                hospitalID,
                firstName,
                lastName,
                email,
                phoneNumber,
                doctorIconUrl,
                departments,
            )

        val updatedFirstName = "updatedFirstName"

        val updateDoctor =
            doctor.update(
                hospitalID,
                FirstName(updatedFirstName),
                LastName(lastName),
                Email(email),
                PhoneNumber(phoneNumber),
                DoctorIconUrl(doctorIconUrl),
                departments,
            )

        assertEquals(updatedFirstName, updateDoctor.firstName.value)
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

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Doctor.create(
                    doctorID,
                    hospitalID,
                    firstName,
                    lastName,
                    email,
                    phoneNumber,
                    doctorIconUrl,
                    departments,
                )
            }

        assertEquals("email should be valid", exception.message)
    }
}
