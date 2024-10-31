package com.unicorn.api.domain.hospital

import org.junit.jupiter.api.Test
import java.util.*

class HospitalTest {
    @Test
    fun `should create hospital`() {
        val hospital =
            Hospital.fromStore(
                hospitalID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                hospitalName = "Hospital Name",
                address = "Address",
                postalCode = "Postal Code",
                phoneNumber = "Phone Number",
            )

        assert(hospital.hospitalID.value.toString() == "123e4567-e89b-12d3-a456-426614174000")
        assert(hospital.hospitalName.value == "Hospital Name")
        assert(hospital.address.value == "Address")
        assert(hospital.postalCode.value == "Postal Code")
        assert(hospital.phoneNumber.value == "Phone Number")
    }
}
