package com.unicorn.api.domain.hospital

import java.util.UUID

data class Hospital(
    val hospitalID: HospitalID,
    val hospitalName: HospitalName,
    val address: Address,
    val postalCode: PostalCode,
    val phoneNumber: PhoneNumber,
) {
    companion object {
        fun fromStore(
            hospitalID: UUID,
            hospitalName: String,
            address: String,
            postalCode: String,
            phoneNumber: String,
        ): Hospital {
            return Hospital(
                hospitalID = HospitalID(hospitalID),
                hospitalName = HospitalName(hospitalName),
                address = Address(address),
                postalCode = PostalCode(postalCode),
                phoneNumber = PhoneNumber(phoneNumber),
            )
        }
    }
}

@JvmInline
value class HospitalID(val value: UUID)

@JvmInline
value class HospitalName(val value: String)

@JvmInline
value class Address(val value: String)

@JvmInline
value class PostalCode(val value: String)

@JvmInline
value class PhoneNumber(val value: String)
