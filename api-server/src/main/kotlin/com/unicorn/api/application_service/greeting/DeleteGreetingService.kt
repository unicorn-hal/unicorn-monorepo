package com.unicorn.api.application_service.greeting

import com.unicorn.api.infrastructure.greeting.GreetingRepository
import org.springframework.stereotype.Service
import java.util.*

interface DeleteGreetingService {
    fun delete(id: UUID): Unit
}

@Service
class DeleteGreetingServiceImpl(
    private val greetingRepository: GreetingRepository
) : DeleteGreetingService {
    override fun delete(id: UUID): Unit {
        val greeting = greetingRepository.getOrNullById(id)
            ?: throw IllegalArgumentException("greeting not found")

        greetingRepository.delete(greeting)
    }
}