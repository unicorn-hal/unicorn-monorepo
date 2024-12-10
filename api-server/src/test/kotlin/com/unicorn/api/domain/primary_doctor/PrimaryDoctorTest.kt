package com.unicorn.api.domain.primary_doctor

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class PrimaryDoctorTest {
    @Test
    fun `should create primary doctor`() {
        val primaryDoctor =
            PrimaryDoctor.create(
                userID = "test",
                doctorID = "doctor",
            )

        assertEquals("test", primaryDoctor.userID.value)
        assertEquals("doctor", primaryDoctor.doctorID.value)
    }

    @Test
    fun `should create primary doctor from store`() {
        val primaryDoctor =
            PrimaryDoctor.fromStore(
                primaryDoctorID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5a0"),
                userID = "test",
                doctorID = "doctor",
            )

        assertEquals(UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5a0"), primaryDoctor.primaryDoctorID.value)
        assertEquals("test", primaryDoctor.userID.value)
        assertEquals("doctor", primaryDoctor.doctorID.value)
    }
}
