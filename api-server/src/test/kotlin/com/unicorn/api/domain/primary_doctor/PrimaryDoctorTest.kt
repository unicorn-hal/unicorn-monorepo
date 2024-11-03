package com.unicorn.api.domain.primary_doctor

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PrimaryDoctorsTest {

    @Test
    fun `create PrimaryDoctors with initial doctors`() {
        val userID = UserID("test")
        val doctorID = DoctorID("doctor")

        val primaryDoctors = PrimaryDoctors.create(userID, listOf(doctorID))

        assertNotNull(primaryDoctors)
        assertEquals(1, primaryDoctors.doctors.size)
        assertEquals(doctorID, primaryDoctors.doctors[0].doctorID)
    }

    @Test
    fun `add a new doctor using fromStore`() {
        val userID = UserID("test")
        val initialDoctorID = DoctorID("doctor")

        var primaryDoctors = PrimaryDoctors.create(userID, listOf(initialDoctorID))

        val newDoctorID = DoctorID("doctor2")
        primaryDoctors = PrimaryDoctors.fromStore(userID, primaryDoctors.doctors, newDoctorID)

        assertEquals(2, primaryDoctors.doctors.size)
        assertEquals(initialDoctorID, primaryDoctors.doctors[0].doctorID)
        assertEquals(newDoctorID, primaryDoctors.doctors[1].doctorID)
    }

    @Test
    fun `update doctors list`() {
        val userID = UserID("test")
        val initialDoctorID = DoctorID("doctor")
        val primaryDoctors = PrimaryDoctors.create(userID, listOf(initialDoctorID))

        val newDoctorID = DoctorID("doctor2")
        val newDoctorIDs = listOf(newDoctorID)
        val updatePrimaryDoctors = primaryDoctors.updateDoctors(newDoctorIDs)

        assertEquals(1, updatePrimaryDoctors.doctors.size)
    }

    @Test
    fun `update doctors with no common doctors between existing and new list`() {
        val userID = UserID("test")
        val existingDoctorID1 = DoctorID("doctor1")
        val existingDoctorID2 = DoctorID("doctor2")
        val primaryDoctors = PrimaryDoctors.create(userID, listOf(existingDoctorID1, existingDoctorID2))

        // newDoctorIDsには存在しないドクターIDを指定
        val newDoctorIDs = listOf(DoctorID("doctor3"), DoctorID("doctor4"))
        val updatePrimaryDoctors = primaryDoctors.updateDoctors(newDoctorIDs)

        assertEquals(2, updatePrimaryDoctors.doctors.size)
    }

    @Test
    fun `update doctors with all existing doctors in new list`() {
        val userID = UserID("test")
        val existingDoctorID1 = DoctorID("doctor1")
        val existingDoctorID2 = DoctorID("doctor2")
        val primaryDoctors = PrimaryDoctors.create(userID, listOf(existingDoctorID1, existingDoctorID2))

        // newDoctorIDsに既存のドクターIDをすべて指定
        val newDoctorIDs = listOf(existingDoctorID1, existingDoctorID2)
        val updatePrimaryDoctors = primaryDoctors.updateDoctors(newDoctorIDs)

        assertEquals(2, updatePrimaryDoctors.doctors.size)
    }

    @Test
    fun `update doctors and categorize changes`() {
        val userID = UserID("test")
        val existingDoctorID1 = DoctorID("doctor1")
        val existingDoctorID2 = DoctorID("doctor2")
        val primaryDoctors = PrimaryDoctors.create(userID, listOf(existingDoctorID1, existingDoctorID2))

        // newDoctorIDsには既存のドクターIDと新しいドクターIDを指定
        val newDoctorIDs = listOf(existingDoctorID1, DoctorID("doctor3"))
        val updatePrimaryDoctors = primaryDoctors.updateDoctors(newDoctorIDs)

        assertEquals(2, updatePrimaryDoctors.doctors.size)
    }
}
