package com.unicorn.api.application_service.greeting

import com.unicorn.api.domain.greeting.Greeting
import com.unicorn.api.infrastructure.greeting.GreetingRepository
import org.springframework.stereotype.Service
import java.util.*

interface UpdateGreetingService {
    fun update(id: UUID, message: String): Greeting
}

@Service
class UpdateGreetingServiceImpl(private val greetingRepository: GreetingRepository) : UpdateGreetingService {
    override fun update(id: UUID, message: String): Greeting {
        val greeting = greetingRepository.getOrNullById(id)
            ?: throw IllegalArgumentException("greeting not found")

        val updatedGreeting = greeting.changeMessage(message)
        return greetingRepository.update(updatedGreeting)
    }
}