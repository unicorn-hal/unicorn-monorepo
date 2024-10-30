package com.unicorn.api.domain.primary_doctor

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PrimaryDoctorsTest {

    @Test
    fun `create PrimaryDoctors with initial doctors`() {
        val userID = UserID("user-01")
        val doctorID = DoctorID("doctor-01")

        val primaryDoctors = PrimaryDoctors.create(userID, listOf(doctorID))

        assertNotNull(primaryDoctors)
        assertEquals(1, primaryDoctors.doctors.size)
        assertEquals(doctorID, primaryDoctors.doctors[0].doctorID)
    }

    @Test
    fun `add a new doctor using fromStore`() {
        val userID = UserID("user-01")
        val initialDoctorID = DoctorID("doctor-01")

        var primaryDoctors = PrimaryDoctors.create(userID, listOf(initialDoctorID))

        val newDoctorID = DoctorID("doctor-02")
        primaryDoctors = primaryDoctors.fromStore(newDoctorID)

        assertEquals(2, primaryDoctors.doctors.size)
        assertEquals(initialDoctorID, primaryDoctors.doctors[0].doctorID)
        assertEquals(newDoctorID, primaryDoctors.doctors[1].doctorID)
    }

    @Test
    fun `update doctors list`() {
        val userID = UserID("user-01")
        val initialDoctorID = DoctorID("doctor-01")
        val primaryDoctors = PrimaryDoctors.create(userID, listOf(initialDoctorID))

        val newDoctorID = DoctorID("doctor-02")
        val updatedDoctors = listOf(
            PrimaryDoctor(primaryDoctorID = PrimaryDoctorID.newID(), doctorID = initialDoctorID),
            PrimaryDoctor(primaryDoctorID = PrimaryDoctorID.newID(), doctorID = newDoctorID)
        )
        val updatedPrimaryDoctors = primaryDoctors.update(updatedDoctors)

        assertEquals(2, updatedPrimaryDoctors.doctors.size)
        assertEquals(initialDoctorID, updatedPrimaryDoctors.doctors[0].doctorID)
        assertEquals(newDoctorID, updatedPrimaryDoctors.doctors[1].doctorID)
    }
}
