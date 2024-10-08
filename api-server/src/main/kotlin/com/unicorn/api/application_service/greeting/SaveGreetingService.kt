package com.unicorn.api.application_service.greeting

import com.unicorn.api.domain.greeting.Greeting
import com.unicorn.api.infrastructure.greeting.GreetingRepository
import org.springframework.stereotype.Service

interface SaveGreetingService {
    fun save(message: String): Greeting
}

@Service
class SaveGreetingServiceImpl(private val greetingRepository: GreetingRepository) : SaveGreetingService {
    override fun save(message: String): Greeting {
        val greeting = Greeting.create(message)
        return greetingRepository.store(greeting)
    }
}