package com.unicorn.api.infrastructure.primary_doctor

import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.primary_doctor.*
import com.unicorn.api.domain.user.User
import com.unicorn.api.domain.user.UserID
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
import java.time.LocalDate
import java.util.*

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

    private fun findPrimaryDoctorBy(primaryDoctorID: PrimaryDoctorID): PrimaryDoctor? {
        // language=postgresql
        val sql =
            """
            SELECT
                primary_doctor_id,
                user_id,
                doctor_id
            FROM primary_doctors
            WHERE primary_doctor_id = :primaryDoctorID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("primaryDoctorID", primaryDoctorID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            PrimaryDoctor.fromStore(
                primaryDoctorID = UUID.fromString(rs.getString("primary_doctor_id")),
                userID = rs.getString("user_id"),
                doctorID = rs.getString("doctor_id"),
            )
        }.singleOrNull()
    }

    private fun findPrimaryDoctorByUser(user: User): List<PrimaryDoctor> {
        // language=postgresql
        val sql =
            """
            SELECT
                primary_doctor_id,
                user_id,
                doctor_id
            FROM primary_doctors
            WHERE user_id = :userID
            AND deleted_at IS NULL
            """.trimIndent()

        val sqlParams =
            MapSqlParameterSource()
                .addValue("userID", user.userID.value)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            PrimaryDoctor.fromStore(
                primaryDoctorID = UUID.fromString(rs.getString("primary_doctor_id")),
                userID = rs.getString("user_id"),
                doctorID = rs.getString("doctor_id"),
            )
        }
    }

    @Test
    fun `should store primary doctor`() {
        val primaryDoctor =
            PrimaryDoctor.create(
                userID = "test",
                doctorID = "doctor",
            )
        primaryDoctorRepository.store(primaryDoctor)

        val foundPrimaryDoctor = findPrimaryDoctorBy(primaryDoctor.primaryDoctorID)
        assertNotNull(foundPrimaryDoctor)
        assertEquals(primaryDoctor, foundPrimaryDoctor)
    }

    @Test
    fun `should find primary doctor by ID`() {
        val primaryDoctorID = PrimaryDoctorID(UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5a0"))

        val foundPrimaryDoctor = primaryDoctorRepository.getOrNullBy(primaryDoctorID)

        assertNotNull(foundPrimaryDoctor)
        assertEquals(primaryDoctorID, foundPrimaryDoctor!!.primaryDoctorID)
        assertEquals("test", foundPrimaryDoctor.userID.value)
        assertEquals("doctor", foundPrimaryDoctor.doctorID.value)
    }

    @Test
    fun `should find primary doctor by user ID`() {
        val userID = UserID("test")

        val foundPrimaryDoctors = primaryDoctorRepository.getOrNullByUserID(userID)

        assertNotNull(foundPrimaryDoctors)
        assertEquals(2, foundPrimaryDoctors.size)
        assertEquals("test", foundPrimaryDoctors[0].userID.value)
        assertEquals("doctor", foundPrimaryDoctors[0].doctorID.value)
        assertEquals("test", foundPrimaryDoctors[1].userID.value)
        assertEquals("doctor2", foundPrimaryDoctors[1].doctorID.value)
    }

    @Test
    fun `should not find primary doctor by not found ID`() {
        val primaryDoctorID = PrimaryDoctorID(UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5a1"))

        val foundPrimaryDoctor = primaryDoctorRepository.getOrNullBy(primaryDoctorID)

        assertNull(foundPrimaryDoctor)
    }

    @Test
    fun `should not find primary doctor by not found user ID`() {
        val userID = UserID("notFound")

        val foundPrimaryDoctors = primaryDoctorRepository.getOrNullByUserID(userID)

        assertEquals(0, foundPrimaryDoctors.size)
    }

    @Test
    fun `should delete primary doctor`() {
        val primaryDoctor = PrimaryDoctor.create("test", "doctor")

        primaryDoctorRepository.store(primaryDoctor)
        primaryDoctorRepository.delete(primaryDoctor)

        val foundPrimaryDoctor = findPrimaryDoctorBy(primaryDoctor.primaryDoctorID)
        assertNull(foundPrimaryDoctor)
    }

    @Test
    fun `should found primary doctor by doctor ID and user ID`() {
        val userID = UserID("test")
        val doctorID = DoctorID("doctor")

        val foundPrimaryDoctor = primaryDoctorRepository.getOrNullByDoctorIDAndUserID(doctorID, userID)

        assertNotNull(foundPrimaryDoctor)
        assertEquals("test", foundPrimaryDoctor!!.userID.value)
        assertEquals("doctor", foundPrimaryDoctor.doctorID.value)
    }

    @Test
    fun `should not found primary doctor by not found doctor ID and user ID`() {
        val userID = UserID("notFoundTest")
        val doctorID = DoctorID("notFoundDoctor")

        val foundPrimaryDoctor = primaryDoctorRepository.getOrNullByDoctorIDAndUserID(doctorID, userID)

        assertNull(foundPrimaryDoctor)
    }

    @Test
    fun `should not found deleted primary doctor by doctor ID and user ID`() {
        val userID = UserID("testUser")
        val doctorID = DoctorID("doctor7")

        val foundPrimaryDoctor = primaryDoctorRepository.getOrNullByDoctorIDAndUserID(doctorID, userID)

        assertNull(foundPrimaryDoctor)
    }

    @Test
    fun `should delete message by user`() {
        val user =
            User.fromStore(
                userID = "test",
                firstName = "test",
                lastName = "test",
                email = "sample@test.com",
                birthDate = LocalDate.of(1990, 1, 1),
                gender = "male",
                address = "test",
                postalCode = "0000000",
                phoneNumber = "00000000000",
                iconImageUrl = "https://example.com",
                bodyHeight = 170.4,
                bodyWeight = 60.4,
                occupation = "test",
            )

        primaryDoctorRepository.deleteByUser(user)

        val deletedPrimaryDoctor = findPrimaryDoctorByUser(user)
        assertEquals(0, deletedPrimaryDoctor.size)
    }
}
