package com.unicorn.api.infrastructure.primary_doctor

import com.unicorn.api.domain.primary_doctor.PrimaryDoctorID
import com.unicorn.api.domain.primary_doctor.PrimaryDoctors
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.domain.primary_doctor.PrimaryDoctor
import com.unicorn.api.domain.user.UserID
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
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
            PrimaryDoctor.fromStore(
                primaryDoctorID = PrimaryDoctorID(UUID.fromString(rs.getString("primary_doctor_id"))),
                doctorID = DoctorID(rs.getString("doctor_id"))
            )
        }.singleOrNull()
    }

    @Test
    fun `should get primary doctor by primaryDoctorID`() {
        val userID = UserID("test")
        val primaryDoctorID = PrimaryDoctorID(UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5a0"))

        val primaryDoctor = primaryDoctorRepository.getOrNullBy(userID, primaryDoctorID)

        assertNotNull(primaryDoctor)

        // primaryDoctorがnullでないことが保証された後に、doctorsにアクセスする
        if (primaryDoctor != null) {
            assertTrue(primaryDoctor.doctors.any { it.primaryDoctorID == primaryDoctorID } )
        }
    }

    @Test
    fun `should return null when primary doctor ID not found`() {
        val userID = UserID("test")
        val primaryDoctorID = PrimaryDoctorID(UUID.fromString("e9c29e5b-9b7a-4d4d-8f59-1c8c7a7c92d4"))

        val act = primaryDoctorRepository.getOrNullBy(userID, primaryDoctorID)

        assertEquals(null, act)
    }

    @Test
    fun `should store multiple primary doctors`() {
        val userID = UserID("test")
        val doctorIDs = listOf(DoctorID("doctor3"), DoctorID("doctor5"))
        val primaryDoctors = PrimaryDoctors.create(userID, doctorIDs)

        val storedDoctors = primaryDoctorRepository.store(primaryDoctors)

        assertEquals(primaryDoctors.doctors.size, storedDoctors.doctors.size)
        assertTrue(primaryDoctors.doctors[0].primaryDoctorID == storedDoctors.doctors[0].primaryDoctorID)
        assertTrue(primaryDoctors.doctors[0].doctorID == storedDoctors.doctors[0].doctorID)
        assertTrue(primaryDoctors.doctors[1].primaryDoctorID == storedDoctors.doctors[1].primaryDoctorID)
        assertTrue(primaryDoctors.doctors[1].doctorID == storedDoctors.doctors[1].doctorID)
    }

    @Test
    fun `should update existing primary doctor using store`() {
        // 既存の医者を更新するテスト
        val existingPrimaryDoctorID = PrimaryDoctorID(UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5a0"))
        val existingDoctor = findPrimaryDoctorByPrimaryDoctorID(existingPrimaryDoctorID)
        assertNotNull(existingDoctor)

        // 新しい医者の情報に更新する
        val userID = UserID("test")
        val updatedDoctorIDs = listOf(DoctorID("doctor2"))
        val toUpdateDoctors = PrimaryDoctors.create(userID, updatedDoctorIDs)

        val updatedDoctors = primaryDoctorRepository.store(toUpdateDoctors)

        assertTrue(updatedDoctors.doctors.size == 1)
        assertTrue(updatedDoctors.doctors[0].primaryDoctorID != existingPrimaryDoctorID)
        if (existingDoctor != null) {
            assertTrue(updatedDoctors.doctors[0].doctorID != existingDoctor.doctorID)
        }
    }

    @Test
    fun `should handle case when commonDoctors is null and both doctorsToAdd and doctorsToDelete are empty`() {
        val userID = UserID("test")
        val primaryDoctors = PrimaryDoctors.create(userID, emptyList()) // 既存の医者がいない状態を模擬
        val storedDoctors = primaryDoctorRepository.store(primaryDoctors)

        assertTrue(storedDoctors.doctors.isEmpty()) // 追加される医者がいないことを確認
    }

    @Test
    fun `should get primary doctors by userID`() {
        val userID = UserID("test")

        // userIDに関連するすべてのPrimaryDoctorを取得
        val primaryDoctors = primaryDoctorRepository.getOrNullByUserID(userID)

        assertNotNull(primaryDoctors) // PrimaryDoctorsオブジェクトがnullでないことを確認
        assertTrue(primaryDoctors?.doctors?.isNotEmpty() == true) // doctorsが空でないことを確認
    }

    @Test
    fun `should return null when no primary doctors found for userID`() {
        val userID = UserID("nonexistent_user") // 存在しないユーザーIDを指定

        // userIDに関連するPrimaryDoctorが存在しない場合
        val primaryDoctors = primaryDoctorRepository.getOrNullByUserID(userID)

        assertEquals(null, primaryDoctors) // nullが返されることを確認
    }
}
