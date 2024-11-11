package com.unicorn.api.domain.chronic_disease

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class ChronicDiseaseTest {
    @Test
    fun `should create chronic disease`() {
        val chronicDisease =
            ChronicDisease.create(
                userID = "test",
                diseaseID = 1,
            )
        assertEquals("test", chronicDisease.userID.value)
        assertEquals(1, chronicDisease.diseaseID.value)
    }

    @Test
    fun `should create chronic disease from store`() {
        val chronicDisease =
            ChronicDisease.fromStore(
                chronicDiseaseID = UUID.randomUUID(),
                userID = "test",
                diseaseID = 1,
            )
        assertEquals("test", chronicDisease.userID.value)
        assertEquals(1, chronicDisease.diseaseID.value)
    }

    @Test
    fun `should return error message when null diseaseID`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                ChronicDisease.create(
                    userID = "test",
                    diseaseID = 0,
                )
            }
        assertEquals("DiseaseID must be greater than 0", exception.message)
    }
}
