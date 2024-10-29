package com.unicorn.api.domain.primary_doctor

import com.unicorn.api.domain.department.DepartmentID
import com.unicorn.api.domain.doctor.Doctor
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.hospital.HospitalID
import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class PrimaryDoctorTest {

    @Test
    fun `create primary doctor`() {
        val primaryDoctorID = PrimaryDoctorID(UUID.randomUUID())
        val userID = UserID("user-01")
        val doctorID = DoctorID("doctor-01")

        val primaryDoctor = PrimaryDoctors(
            primaryDoctorID = primaryDoctorID,
            userID = userID,
            doctorID = doctorID
        )

        val primaryDoctorList = PrimaryDoctor.create(listOf(primaryDoctor))

        assertNotNull(primaryDoctorList)
        assertEquals(1, primaryDoctorList.primaryDoctors.size)
        assertEquals(primaryDoctorID, primaryDoctorList.primaryDoctors[0].primaryDoctorID)
        assertEquals(userID, primaryDoctorList.primaryDoctors[0].userID)
        assertEquals(doctorID, primaryDoctorList.primaryDoctors[0].doctorID)
    }

    @Test
    fun `test fromStore method`() {
        val primaryDoctorID = UUID.randomUUID()
        val userID = "user-1"
        val doctorID = "doctor-1"

        val primaryDoctorList = PrimaryDoctor.fromStore(primaryDoctorID, userID, doctorID)

        assertNotNull(primaryDoctorList)
        assertEquals(1, primaryDoctorList.primaryDoctors.size)
        assertEquals(primaryDoctorID, primaryDoctorList.primaryDoctors[0].primaryDoctorID.value)
        assertEquals(userID, primaryDoctorList.primaryDoctors[0].userID.value)
        assertEquals(doctorID, primaryDoctorList.primaryDoctors[0].doctorID.value)
    }

    @Test
    fun `test update primary doctor list`() {
        val primaryDoctorID1 = PrimaryDoctorID(UUID.randomUUID())
        val userID1 = UserID("user-1")
        val doctorID1 = DoctorID("doctor-1")

        val primaryDoctor1 = PrimaryDoctors(
            primaryDoctorID = primaryDoctorID1,
            userID = userID1,
            doctorID = doctorID1
        )

        val primaryDoctor = PrimaryDoctor.create(listOf(primaryDoctor1))

        val primaryDoctorID2 = PrimaryDoctorID(UUID.randomUUID())
        val userID2 = UserID("user-2")
        val doctorID2 = DoctorID("doctor-2")

        val primaryDoctor2 = PrimaryDoctors(
            primaryDoctorID = primaryDoctorID2,
            userID = userID2,
            doctorID = doctorID2
        )

        val updatedPrimaryDoctor = primaryDoctor.update(listOf(primaryDoctor1, primaryDoctor2))

        assertEquals(2, updatedPrimaryDoctor.primaryDoctors.size)
        assertEquals(primaryDoctorID1, updatedPrimaryDoctor.primaryDoctors[0].primaryDoctorID)
        assertEquals(userID1, updatedPrimaryDoctor.primaryDoctors[0].userID)
        assertEquals(doctorID1, updatedPrimaryDoctor.primaryDoctors[0].doctorID)
        assertEquals(primaryDoctorID2, updatedPrimaryDoctor.primaryDoctors[1].primaryDoctorID)
        assertEquals(userID2, updatedPrimaryDoctor.primaryDoctors[1].userID)
        assertEquals(doctorID2, updatedPrimaryDoctor.primaryDoctors[1].doctorID)

    }

    @Test
    fun `test toResponse method`() {
        val primaryDoctor1 = PrimaryDoctors(
            primaryDoctorID = PrimaryDoctorID(UUID.randomUUID()),
            userID = UserID("user-01"),
            doctorID = DoctorID("doctor-01")
        )
        val primaryDoctor2 = PrimaryDoctors(
            primaryDoctorID = PrimaryDoctorID(UUID.randomUUID()),
            userID = UserID("user-01"),
            doctorID = DoctorID("doctor-02")
        )

        val primaryDoctorList = PrimaryDoctor.create(listOf(primaryDoctor1, primaryDoctor2))

        val response = primaryDoctorList.toResponse()

        assertNotNull(response)

        val expectedUserID = primaryDoctor1.userID.value
        val expectedDoctorIDs = listOf(primaryDoctor1.doctorID.value, primaryDoctor2.doctorID.value)

        assertEquals(expectedUserID, response.userID.value)
        assertEquals(expectedDoctorIDs.joinToString(","), response.doctorIDs.map { it.value }.joinToString(","))
    }
}