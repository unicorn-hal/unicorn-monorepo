package com.unicorn.api.domain.hospital

data class Hospital(
    val hospitalID: String,
    val hospitalName: String,
    val address: String,
    val postalCode: String,
    val phoneNumber: String
)
