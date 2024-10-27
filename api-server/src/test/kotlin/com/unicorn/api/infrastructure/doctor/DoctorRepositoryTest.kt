package com.unicorn.api.infrastructure.doctor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.unicorn.api.domain.doctor.Doctor
import com.unicorn.api.domain.doctor.DoctorID
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
@Sql("/db/doctor/Insert_Parent_Account_Data.sql")
@Sql("/db/hospital/Insert_Hospital_Data.sql")
@Sql("/db/department/Insert_Department_Data.sql")
@Sql("/db/doctor/Insert_Doctor_Data.sql")
@Sql("/db/doctor_department/Insert_Doctor_Department_Data.sql")
class DoctorRepositoryTest {

    @Autowired
    private lateinit var doctorRepository: DoctorRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findUserByDoctorID(doctorID: String): Doctor? {
        // language=postgresql
        val selectSql = """
            SELECT
                doctors.doctor_id,
                hospital_id,
                email,
                phone_number,
                first_name,
                last_name,
                doctor_icon_url,
                JSONB_AGG(dd.department_id) as departments
            FROM doctors
            LEFT JOIN doctor_departments dd on doctors.doctor_id = dd.doctor_id
            WHERE doctors.doctor_id = :doctorID
                AND doctors.deleted_at IS NULL
                AND dd.deleted_at IS NULL
            GROUP BY doctors.doctor_id
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("doctorID", doctorID)

        return namedParameterJdbcTemplate.query(selectSql, sqlParams) { rs, _ ->

            Doctor.fromStore(
                doctorID = rs.getString("doctor_id"),
                hospitalID = UUID.fromString(rs.getString("hospital_id")),
                email = rs.getString("email"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                phoneNumber = rs.getString("phone_number"),
                doctorIconUrl = rs.getString("doctor_icon_url"),
                departments =  jacksonObjectMapper().readValue<List<UUID>>(rs.getString("departments"))
            )
        }.singleOrNull()
    }

    @Test
    fun `should get doctor by doctorID`() {
        val doctorID = DoctorID("doctor")
        val doctor = Doctor.fromStore(
            doctorID = "doctor",
            hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
            firstName = "test",
            lastName = "test",
            email = "test@test.com",
            phoneNumber = "1234567890",
            doctorIconUrl = "https://example.com",
            departments = listOf(UUID.fromString("b68a87a3-b7f1-4b85-b0ab-6c620d68d791"))
        )

        val act = doctorRepository.getOrNullBy(doctorID)

        assertNotNull(act)
        assertEquals(doctor, act)
    }

    @Test
    fun `should return null when doctor not found`() {
        val doctorID = DoctorID("not-found")

        val act = doctorRepository.getOrNullBy(doctorID)

        assertEquals(null, act)
    }

    @Test
    fun `should store doctor`() {
        val doctor = Doctor.fromStore(
            doctorID = "doctor3",
            hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
            firstName = "test",
            lastName = "test",
            email = "test@test.com",
            phoneNumber = "1234567890",
            doctorIconUrl = "https://example.com",
            departments = listOf(UUID.fromString("b68a87a3-b7f1-4b85-b0ab-6c620d68d791")),
        )

        doctorRepository.store(doctor)

        val act = findUserByDoctorID(doctor.doctorID.value)
        assertNotNull(act)
        assertEquals(doctor, act)
    }

    @Test
    fun `should delete doctor`() {
        val doctorID = DoctorID("doctor")

        doctorRepository.delete(doctorID)

        val act = findUserByDoctorID(doctorID.value)
        assertEquals(null, act)
    }

    @Test
    fun `should update doctor`() {
        val doctorID = DoctorID("doctor")
        val doctor = Doctor.fromStore(
            doctorID = "doctor",
            hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
            firstName = "testtest",
            lastName = "testtest",
            email = "test@test.com",
            phoneNumber = "1234567890",
            doctorIconUrl = "https://example.com",
            departments = listOf(UUID.fromString("cd273b1b-0c3b-4b89-b2b9-01b21832b44c"))
        )

        doctorRepository.store(doctor)

        val act = findUserByDoctorID(doctorID.value)
        assertNotNull(act)
        assertEquals(doctor, act)
    }
}