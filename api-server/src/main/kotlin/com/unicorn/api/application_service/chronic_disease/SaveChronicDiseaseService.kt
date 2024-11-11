package com.unicorn.api.application_service.chronic_disease

import com.unicorn.api.controller.chronic_disease.ChronicDiseasePostRequest
import com.unicorn.api.domain.chronic_disease.ChronicDisease
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.chronic_disease.ChronicDiseaseRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import java.util.*

interface SaveChronicDiseaseService {
    fun save(
        userID: UserID,
        chronicDiseasePostRequest: ChronicDiseasePostRequest,
    ): ChronicDisease
}

@Service
class SaveChronicDiseaseServiceImpl(
    private val userRepository: UserRepository,
    private val chronicDiseaseRepository: ChronicDiseaseRepository,
) : SaveChronicDiseaseService {
    override fun save(
        userID: UserID,
        chronicDiseasePostRequest: ChronicDiseasePostRequest,
    ): ChronicDisease {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val chronicDisease =
            ChronicDisease.create(
                userID = userID.value,
                diseaseID = chronicDiseasePostRequest.diseaseID,
            )
        chronicDiseaseRepository.store(chronicDisease)
        return chronicDisease
    }
}
