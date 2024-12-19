package com.unicorn.api.infrastructure.department

import com.unicorn.api.domain.department.DepartmentID
import com.unicorn.api.domain.doctor.Doctor
import org.junit.jupiter.api.Assertions.assertEquals
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
@Sql("/db/primary_doctor/Insert_Parent_Account_Data.sql")
@Sql("/db/primary_doctor/Insert_User_Data.sql")
@Sql("/db/primary_doctor/Insert_Hospital_Data.sql")
@Sql("/db/primary_doctor/Insert_Department_Data.sql")
@Sql("/db/primary_doctor/Insert_Doctor_Data.sql")
@Sql("/db/primary_doctor/Insert_Doctor_Department_Data.sql")
@Sql("/db/primary_doctor/Insert_PrimaryDoctor_Data.sql")
class DepartmentRepositoryTest {
    @Autowired
    private lateinit var departmentRepository: DepartmentRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun getBy(doctor: Doctor): List<DoctorDepartment> {
        // language=postgresql
        val sql =
            """
            SELECT doctor_department_id, department_id, doctor_id
            FROM doctor_departments
            WHERE doctor_id = :doctorID
            AND deleted_at IS  NULL
            """.trimIndent()

        val params =
            MapSqlParameterSource()
                .addValue("doctorID", doctor.doctorID.value)

        return namedParameterJdbcTemplate.query(sql, params) { rs, _ ->
            DoctorDepartment(
                doctorDepartmentID = UUID.fromString(rs.getString("doctor_department_id")),
                departmentID = UUID.fromString(rs.getString("department_id")),
                doctorID = rs.getString("doctor_id"),
            )
        }
    }

    @Test
    fun `should get department by departmentID`() {
        val departmentID = DepartmentID(UUID.fromString("a1dcb69e-472f-4a57-90a2-f2c63b62ec90"))

        val department = departmentRepository.getOrNullBy(departmentID)

        assert(department != null)
        assert(department!!.departmentID == departmentID)
        assert(department.departmentName.value == "総合内科")
    }

    @Test
    fun `should get departments by departmentIDs`() {
        val departmentIDs =
            listOf(
                DepartmentID(UUID.fromString("a1dcb69e-472f-4a57-90a2-f2c63b62ec90")),
                DepartmentID(UUID.fromString("b68a87a3-b7f1-4b85-b0ab-6c620d68d791")),
            )

        val departments = departmentRepository.findByDepartmentIDs(departmentIDs)

        assert(departments.size == 2)
        assert(departments[0] != null)
        assert(departments[0]!!.departmentID == departmentIDs[0])
        assert(departments[0]!!.departmentName.value == "総合内科")
        assert(departments[1] != null)
        assert(departments[1]!!.departmentID == departmentIDs[1])
        assert(departments[1]!!.departmentName.value == "循環器内科")
    }

    @Test
    fun `should return null when department does not exist`() {
        val departmentID = DepartmentID(UUID.fromString("00000000-0000-0000-0000-000000000000"))

        val department = departmentRepository.getOrNullBy(departmentID)

        assert(department == null)
    }

    @Test
    fun `should return null when departmentIDs do not exist`() {
        val departmentIDs =
            listOf(
                DepartmentID(UUID.fromString("a1dcb69e-472f-4a57-90a2-f2c63b62ec90")),
                DepartmentID(UUID.fromString("11111111-1111-1111-1111-111111111111")),
            )

        val departments = departmentRepository.findByDepartmentIDs(departmentIDs)

        assert(departments.size == 2)
        assert(departments[0] != null)
        assert(departments[1] == null)
    }

    @Test
    fun `should delete doctor departments by doctor `() {
        val doctor =
            Doctor.fromStore(
                doctorID = "doctor",
                hospitalID = UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"),
                firstName = "test",
                lastName = "test",
                email = "test@test.com",
                phoneNumber = "1234567890",
                doctorIconUrl = "https://example.com",
                departments = listOf(UUID.fromString("b68a87a3-b7f1-4b85-b0ab-6c620d68d791")),
            )

        departmentRepository.deleteByDoctor(doctor)

        val deletedDoctorDepartments = getBy(doctor)
        assertEquals(0, deletedDoctorDepartments.size)
    }
}

data class DoctorDepartment(
    val doctorDepartmentID: UUID,
    val departmentID: UUID,
    val doctorID: String,
)
