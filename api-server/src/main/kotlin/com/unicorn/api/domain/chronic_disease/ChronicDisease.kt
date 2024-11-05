package com.unicorn.api.domain.chronic_disease

import com.unicorn.api.domain.user.UserID
import java.util.*

data class ChronicDisease private constructor(
    val chronicDiseaseID: ChronicDiseaseID,
    val userID: UserID,
    val diseaseID: DiseaseID,
) {
    companion object {
        fun fromStore(
            chronicDiseaseID: UUID,
            userID: String,
            diseaseID: Int,
        ): ChronicDisease {
            return ChronicDisease(
                chronicDiseaseID = ChronicDiseaseID(chronicDiseaseID),
                userID = UserID(userID),
                diseaseID = DiseaseID(diseaseID),
            )
        }

        fun create(
            userID: String,
            diseaseID: Int,
        ): ChronicDisease {
            return ChronicDisease(
                chronicDiseaseID = ChronicDiseaseID(UUID.randomUUID()),
                userID = UserID(userID),
                diseaseID = DiseaseID(diseaseID),
            )
        }
    }
}

@JvmInline
value class ChronicDiseaseID(val value: UUID)

@JvmInline
value class DiseaseID(val value: Int) {
    init {
        require(value > 0) { "DiseaseID must be greater than 0" }
    }
}
