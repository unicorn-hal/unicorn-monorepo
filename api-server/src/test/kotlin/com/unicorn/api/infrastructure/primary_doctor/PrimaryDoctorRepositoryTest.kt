package com.unicorn.api.infrastructure.primary_doctor

import com.unicorn.api.domain.primary_doctor.PrimaryDoctorID
import com.unicorn.api.domain.primary_doctor.PrimaryDoctors
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.primary_doctor.PrimaryDoctor
import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Assertions.assertNotNull
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
import kotlin.test.assertEquals

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/primary_doctor/Insert_Parent_Account_Data.sql")
@Sql("/db/primary_doctor/Insert_User_Data.sql")
@Sql("/db/primary_doctor/Insert_Hospital_Data.sql")
@Sql("/db/primary_doctor/Insert_Department_Data.sql")
@Sql("/db/primary_doctor/Insert_Doctor_Data.sql")
@Sql("/db/primary_doctor/Insert_Doctor_Department_Data.sql")
@Sql("/db/primary_doctor/Insert_PrimaryDoctor_Data.sql")
class PrimaryDoctorRepositoryTest {

    @Autowired
    private lateinit var primaryDoctorRepository: PrimaryDoctorRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findPrimaryDoctorByPrimaryDoctorID(primaryDoctorID: PrimaryDoctorID): PrimaryDoctor? {
        // language=postgresql
        val sql = """
        SELECT primary_doctor_id, doctor_id, user_id
        FROM primary_doctors
        WHERE primary_doctor_id = :primaryDoctorId
        AND deleted_at IS NULL
    """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("primaryDoctorId", primaryDoctorID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            PrimaryDoctor(
                primaryDoctorID = PrimaryDoctorID(rs.getObject("primary_doctor_id", UUID::class.java)),
                doctorID = DoctorID(rs.getString("doctor_id"))
            )
        }.singleOrNull()
    }

    @Test
    fun `should get primary doctor by primaryDoctorID`() {
        val primaryDoctorID = PrimaryDoctorID(UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5a0"))

        val primaryDoctor = findPrimaryDoctorByPrimaryDoctorID(primaryDoctorID)

        assertNotNull(primaryDoctor)
        assertEquals(primaryDoctorID, primaryDoctor?.primaryDoctorID)
    }

    @Test
    fun `should return null when primary doctor ID not found`() {
        val primaryDoctorID = PrimaryDoctorID(UUID.fromString("e9c29e5b-9b7a-4d4d-8f59-1c8c7a7c92d4"))

        val act = primaryDoctorRepository.getOrNullBy(primaryDoctorID)

        assertEquals(null, act)
    }

    @Test
    fun `should store multiple primary doctors`() {
        val userID = UserID("test")
        val doctorIDs = listOf(DoctorID("doctor"), DoctorID("doctor2"))
        val primaryDoctors = PrimaryDoctors.create(userID, doctorIDs)

        val storedDoctors = primaryDoctorRepository.store(primaryDoctors)

        assertEquals(primaryDoctors.doctors.size, storedDoctors.size)
        primaryDoctors.doctors.forEach { expectedDoctor ->
            val actualDoctor = findPrimaryDoctorByPrimaryDoctorID(expectedDoctor.primaryDoctorID)
            assertNotNull(actualDoctor)
            assertEquals(expectedDoctor, actualDoctor)
        }
    }

    @Test
    fun `should update existing primary doctor using store`() {
        val userID = UserID("test")
        val doctorIDs = listOf(DoctorID("doctor2"))
        val primaryDoctors = PrimaryDoctors.create(userID, doctorIDs)

        primaryDoctorRepository.store(primaryDoctors)

        val updatedDoctorIDs = listOf(DoctorID("doctor"))
        val updatedDoctors = PrimaryDoctors.create(userID, updatedDoctorIDs)

        primaryDoctorRepository.store(updatedDoctors)

        updatedDoctors.doctors.forEach { expectedDoctor ->
            val actualDoctor = findPrimaryDoctorByPrimaryDoctorID(expectedDoctor.primaryDoctorID)
            assertNotNull(actualDoctor)
            assertEquals(expectedDoctor.doctorID, actualDoctor?.doctorID)
        }
    }
}
