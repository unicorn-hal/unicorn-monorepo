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
        val updatedDoctorIDs = listOf(initialDoctorID, newDoctorID)
        val (commonDoctors, doctorsToAdd, doctorsToDelete) = primaryDoctors.updateDoctors(updatedDoctorIDs)

        assertEquals(1, commonDoctors.size)
        assertEquals(initialDoctorID, commonDoctors[0].doctorID)
        assertEquals(1, doctorsToAdd.size)
        assertEquals(newDoctorID, doctorsToAdd[0].doctorID)
        assertEquals(0, doctorsToDelete.size)
    }

    @Test
    fun `update doctors results in commonDoctors being empty`() {
        val userID = UserID("test")
        val existingDoctorID1 = DoctorID("doctor1")
        val existingDoctorID2 = DoctorID("doctor2")
        val primaryDoctors = PrimaryDoctors.create(userID, listOf(existingDoctorID1, existingDoctorID2))

        // newDoctorIDsには存在しないドクターIDを指定
        val newDoctorIDs = listOf(DoctorID("doctor3"), DoctorID("doctor4"))
        val (commonDoctors, doctorsToAdd, doctorsToDelete) = primaryDoctors.updateDoctors(newDoctorIDs)

        assertEquals(0, commonDoctors.size) // 共通するドクターがいない
        assertEquals(2, doctorsToAdd.size) // 新たに追加するドクターが2人
        assertEquals(2, doctorsToDelete.size) // 既存のドクターが2人削除される
    }

    @Test
    fun `update doctors results in both doctorsToAdd and doctorsToDelete being empty`() {
        val userID = UserID("test")
        val existingDoctorID1 = DoctorID("doctor1")
        val existingDoctorID2 = DoctorID("doctor2")
        val primaryDoctors = PrimaryDoctors.create(userID, listOf(existingDoctorID1, existingDoctorID2))

        // newDoctorIDsに既存のドクターIDをすべて指定
        val newDoctorIDs = listOf(existingDoctorID1, existingDoctorID2)
        val (commonDoctors, doctorsToAdd, doctorsToDelete) = primaryDoctors.updateDoctors(newDoctorIDs)

        assertEquals(2, commonDoctors.size) // 共通するドクターが2人
        assertEquals(0, doctorsToAdd.size) // 新たに追加するドクターがいない
        assertEquals(0, doctorsToDelete.size) // 既存のドクターが削除されない
    }

    @Test
    fun `update doctors and categorize changes`() {
        val userID = UserID("test")
        val existingDoctorID1 = DoctorID("doctor1")
        val existingDoctorID2 = DoctorID("doctor2")
        val primaryDoctors = PrimaryDoctors.create(userID, listOf(existingDoctorID1, existingDoctorID2))

        // newDoctorIDsには既存のドクターIDと新しいドクターIDを指定
        val newDoctorIDs = listOf(existingDoctorID1, DoctorID("doctor3"))
        val (commonDoctors, doctorsToAdd, doctorsToDelete) = primaryDoctors.updateDoctors(newDoctorIDs)

        assertEquals(1, commonDoctors.size) // 共通するドクターが1人
        assertEquals(1, doctorsToAdd.size) // 新たに追加するドクターが1人
        assertEquals(1, doctorsToDelete.size) // 既存のドクターのうち1人が削除される
    }
}
