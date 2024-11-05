package com.unicorn.api.infrastructure.chronic_disease

import com.unicorn.api.domain.chronic_disease.*
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
@Sql("/db/disease/Insert_Disease_Data.sql")
@Sql("/db/chronic_disease/Insert_Chronic_Disease_Data.sql")
class ChronicDiseaseRepositoryTest {
    @Autowired
    private lateinit var chronicDiseaseRepository: ChronicDiseaseRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findChronicDiseaseID(chronicDiseaseID: UUID): ChronicDisease? {
        // language=postgresql
        val sql =
            """
            SELECT
                chronic_disease_id,
                user_id,
                disease_id
            FROM chronic_diseases
            WHERE chronic_disease_id = :chronicDiseaseID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("chronicDiseaseID", chronicDiseaseID)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams,
        ) { rs, _ ->
            ChronicDisease.fromStore(
                chronicDiseaseID = UUID.fromString(rs.getString("chronic_disease_id")),
                userID = rs.getString("user_id"),
                diseaseID = rs.getInt("disease_id"),
            )
        }.singleOrNull()
    }

    @Test
    fun `should store chronic disease`() {
        val chronicDisease =
            ChronicDisease.create(
                userID = "test",
                diseaseID = 1,
            )
        chronicDiseaseRepository.store(chronicDisease)
        val storedchronicDisease = findChronicDiseaseID(chronicDisease.chronicDiseaseID.value)
        assertNotNull(storedchronicDisease!!)
        assertEquals("test", storedchronicDisease.userID.value)
        assertEquals(1, storedchronicDisease.diseaseID.value)
    }

    @Test
    fun `should find chronic disease by ID`() {
        val chronicDiseaseID = ChronicDiseaseID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470"))

        val foundChronicDisease = chronicDiseaseRepository.getOrNullBy(chronicDiseaseID)
        assertNotNull(foundChronicDisease!!)
        assertEquals(chronicDiseaseID, foundChronicDisease.chronicDiseaseID)
        assertEquals("test", foundChronicDisease.userID.value)
        assertEquals(1, foundChronicDisease.diseaseID.value)
    }

    @Test
    fun `should not be found if deleted_at is not NULL`() {
        val chronicDiseaseID = ChronicDiseaseID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d471"))

        val foundChronicDisease = chronicDiseaseRepository.getOrNullBy(chronicDiseaseID)
        assertNull(foundChronicDisease)
    }

    @Test
    fun `should not be found if not exist`() {
        val chronicDiseaseID = ChronicDiseaseID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d472"))

        val foundChronicDisease = chronicDiseaseRepository.getOrNullBy(chronicDiseaseID)
        assertNull(foundChronicDisease)
    }

    @Test
    fun `should delete chronic disease`() {
        val chronicDiseaseID = ChronicDiseaseID(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d470"))
        val chronicDisease = chronicDiseaseRepository.getOrNullBy(chronicDiseaseID)

        chronicDiseaseRepository.delete(chronicDisease!!)

        val foundChronicDisease = findChronicDiseaseID(chronicDiseaseID.value)
        assertNull(foundChronicDisease)
    }
}
