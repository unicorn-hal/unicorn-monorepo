package com.unicorn.api.application_service.chronic_disease

import com.unicorn.api.domain.chronic_disease.ChronicDiseaseID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.infrastructure.chronic_disease.ChronicDiseaseRepository
import com.unicorn.api.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

interface DeleteChronicDiseaseService {
    fun delete(
        userID: UserID,
        chronicDiseaseID: ChronicDiseaseID,
    ): Unit

    fun deleteBy(userID: UserID)
}

@Service
class DeleteChronicDiseaseServiceImpl(
    private val userRepository: UserRepository,
    private val chronicDiseaseRepository: ChronicDiseaseRepository,
) : DeleteChronicDiseaseService {
    override fun delete(
        userID: UserID,
        chronicDiseaseID: ChronicDiseaseID,
    ) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        val chronicDisease = chronicDiseaseRepository.getOrNullBy(chronicDiseaseID)
        requireNotNull(chronicDisease) { "Chronic disease not found" }

        chronicDiseaseRepository.delete(chronicDisease)
    }

    override fun deleteBy(userID: UserID) {
        val user = userRepository.getOrNullBy(userID)
        requireNotNull(user) { "User not found" }

        chronicDiseaseRepository.deleteByUser(user)
    }
}
