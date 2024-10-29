package com.unicorn.api.infrastructure.hospital

import com.unicorn.api.domain.hospital.HospitalID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.util.*

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/hospital/Insert_Hospital_Data.sql")
@Sql("/db/hospital/Insert_Deleted_Hospital_Data.sql")
class HospitalRepositoryTest {

    @Autowired
    private lateinit var hospitalRepository: HospitalRepository

    @Test
    fun `should get hospital by hospitalID`() {
        val hospitalID = HospitalID(UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f7"))

        val hospital = hospitalRepository.getOrNullBy(hospitalID)

        assert(hospital != null)
        assert(hospital!!.hospitalID.value.toString() == "d8bfa31d-54b9-4c64-a499-6c522517e5f7")
        assert(hospital.hospitalName.value == "きくち内科医院")
        assert(hospital.address.value == "静岡県静岡市駿河区新川2-8-3")
        assert(hospital.postalCode.value == "4228064")
        assert(hospital.phoneNumber.value == "0542847171")
    }

    @Test
    fun `should return null when hospital does not exist`() {
        val hospitalID = HospitalID(UUID.fromString("d8bfa31d-54b9-4c64-a499-6c522517e5f8"))

        val hospital = hospitalRepository.getOrNullBy(hospitalID)

        assert(hospital == null)
    }

    @Test
    fun `should return null when hospital is deleted`() {
        val hospitalID = HospitalID(UUID.fromString("762a7a7e-41e4-46c2-b36c-f2b302cae3e4"))

        val hospital = hospitalRepository.getOrNullBy(hospitalID)

        assert(hospital == null)
    }
}