package com.unicorn.api.infrastructure.primary_doctor


import com.unicorn.api.domain.primary_doctor.PrimaryDoctor
import com.unicorn.api.domain.primary_doctor.PrimaryDoctorID
import com.unicorn.api.domain.primary_doctor.PrimaryDoctors
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
            .addValue("primaryDoctorId", primaryDoctorID)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->

            PrimaryDoctor.fromStore(
                primaryDoctorID = UUID.fromString("primaryDoctorId"),
                userID = rs.getString("user_id"),
                doctorID = rs.getString("doctor_id")

            )
        }.singleOrNull()
    }

    @Test
    fun `should get primary doctor by primaryDoctorID`() {
        val userID = "test"
        val primaryDoctorID = PrimaryDoctorID(
            UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5a0")
        )

        val primaryDoctor = PrimaryDoctor.fromStore(
            primaryDoctorID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5a0"),
            userID = userID,
            doctorID = "doctor"
        )

        val act = primaryDoctorRepository.getOrNullBy(primaryDoctorID)

        assertNotNull(act)
        assertEquals(primaryDoctor, act)
    }

    @Test
    fun `should return null when primary doctor ID not found`() {
        val primaryDoctorID = PrimaryDoctorID(UUID.fromString("e9c29e5b-9b7a-4d4d-8f59-1c8c7a7c92d4"))

        val act = primaryDoctorRepository.getOrNullBy(primaryDoctorID)

        assertEquals(null, act)
    }

    @Test
    fun `should store multiple primary doctors`() {
        val userID = "test"

        val primaryDoctor1 = PrimaryDoctor.fromStore(
            primaryDoctorID = UUID.fromString("7b42f2f6-1fda-4e3a-b88a-8d4d72b0ae89"),
            userID = userID,
            doctorID = "doctor"
        )

        val primaryDoctor2 = PrimaryDoctor.fromStore(
            primaryDoctorID = UUID.fromString("8c52d3e7-2fda-5f4a-c99b-9d4d82c1be90"),
            userID = userID,
            doctorID = "doctor2"
        )

        val primaryDoctors = listOf(primaryDoctor1, primaryDoctor2)
        primaryDoctorRepository.store(primaryDoctors)

        primaryDoctors.forEach { expectedDoctor ->
            expectedDoctor.primaryDoctors.forEach { doctor: PrimaryDoctors ->
                val actualDoctor = primaryDoctorRepository.getOrNullBy(doctor.primaryDoctorID)
                assertNotNull(actualDoctor)

                val actualPrimaryDoctor = actualDoctor?.primaryDoctors?.firstOrNull()

                assertNotNull(actualPrimaryDoctor)

                assertEquals(doctor, actualPrimaryDoctor)
            }
        }
    }

    @Test
    fun `should update existing primary doctor using store`() {
        val userID = "test"

        val updatedDoctor = PrimaryDoctor.fromStore(
            primaryDoctorID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5a0"),
            userID = userID,
            doctorID = "doctor2"
        )

        val primaryDoctor = PrimaryDoctor.fromStore(
            primaryDoctorID = UUID.fromString("7b42f2f6-1fda-4e3a-b88a-8d4d72b0ae89"),
            userID = userID,
            doctorID = "doctor"
        )

        val primaryDoctors = listOf(updatedDoctor, primaryDoctor)
        primaryDoctorRepository.store(primaryDoctors)

        primaryDoctors.forEach { expectedDoctor ->
            expectedDoctor.primaryDoctors.forEach { doctor: PrimaryDoctors ->
                val actualDoctor = primaryDoctorRepository.getOrNullBy(doctor.primaryDoctorID)
                assertNotNull(actualDoctor)

                val actualPrimaryDoctor = actualDoctor?.primaryDoctors?.firstOrNull()

                assertNotNull(actualPrimaryDoctor)

                assertEquals(doctor, actualPrimaryDoctor)
            }
        }
    }
}