package com.unicorn.api.infrastructure.emergency

import com.unicorn.api.domain.emergency.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/emergency/Insert_Emergency_Data.sql")
class EmergencyRepositoryTest {
    @Autowired
    private lateinit var emergencyRepository: EmergencyRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findEmergencyByID(emergencyID: UUID): Emergency? {
        // language=postgresql
        val sql =
            """
            SELECT 
                emergency_queue_id,
                user_id,
                user_latitude,
                user_longitude
            FROM emergency_queue
            WHERE emergency_queue_id = :emergencyID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("emergencyID", emergencyID)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            Emergency.fromStore(
                emergencyID = UUID.fromString(rs.getString("emergency_queue_id")),
                userID = rs.getString("user_id"),
                userLatitude = rs.getDouble("user_latitude"),
                userLongitude = rs.getDouble("user_longitude"),
            )
        }.singleOrNull()
    }

    @Test
    fun `should store emergency`() {
        val emergency =
            Emergency.create(
                userID = "test",
                userLatitude = 0.0,
                userLongitude = 0.0,
            )

        emergencyRepository.store(emergency)
        val storedEmergency = findEmergencyByID(emergency.emergencyID.value)

        assertNotNull(storedEmergency)
        assertEquals(emergency.userID, storedEmergency?.userID)
        assertEquals(emergency.userLatitude, storedEmergency?.userLatitude)
        assertEquals(emergency.userLongitude, storedEmergency?.userLongitude)
    }

    @Test
    fun `should find emergency by emergencyID`() {
        val emergencyID = EmergencyID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470"))
        val foundEmergency = emergencyRepository.getOrNullBy(emergencyID)

        assertNotNull(foundEmergency)
        assertEquals(emergencyID, foundEmergency!!.emergencyID)
        assertEquals("test", foundEmergency.userID.value)
        assertEquals(1.1, foundEmergency.userLatitude.value)
        assertEquals(1.1, foundEmergency.userLongitude.value)
    }

    @Test
    fun `should not be found if deleted_at is not NULL`() {
        val emergencyID = EmergencyID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d471"))
        val foundEmergency = emergencyRepository.getOrNullBy(emergencyID)

        assertNull(foundEmergency)
    }

    @Test
    fun `should return null when emergency does not exist`() {
        val emergencyID = EmergencyID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d472"))
        val foundEmergency = emergencyRepository.getOrNullBy(emergencyID)
        assertNull(foundEmergency)
    }

    @Test
    fun `should return older emergency`() {
        val olderEmergency = emergencyRepository.getOlderOrNull()
        assertNotNull(olderEmergency)
        assertEquals(2, olderEmergency.size)
        assertEquals("test", olderEmergency[0].userID.value)
        assertEquals(1.1, olderEmergency[0].userLatitude.value)
        assertEquals(1.1, olderEmergency[0].userLongitude.value)
        assertEquals("test", olderEmergency[1].userID.value)
        assertEquals(1.3, olderEmergency[1].userLatitude.value)
        assertEquals(1.3, olderEmergency[1].userLongitude.value)
    }

    @Test
    fun `should return emergency by rowNumber`() {
        val rowNumber = emergencyRepository.getWaitingCount(EmergencyID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d469")))
        assertNotNull(rowNumber)
        assertEquals(2, rowNumber)
    }

    @Test
    fun `should delete emergency`() {
        val emergency =
            Emergency.create(
                userID = "test",
                userLatitude = 0.0,
                userLongitude = 0.0,
            )
        emergencyRepository.store(emergency)
        emergencyRepository.delete(emergency)
        val deletedEmergency = emergencyRepository.getOrNullBy(emergency.emergencyID)
        assertNull(deletedEmergency)
    }
}
