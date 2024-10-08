package com.unicorn.api.controller.greeting

import com.unicorn.api.application_service.greeting.DeleteGreetingService
import com.unicorn.api.application_service.greeting.SaveGreetingService
import com.unicorn.api.application_service.greeting.UpdateGreetingService
import com.unicorn.api.domain.greeting.Greeting
import com.unicorn.api.query_service.greeting.GreetingDto
import com.unicorn.api.query_service.greeting.GreetingQueryService
import com.unicorn.api.query_service.greeting.GreetingResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class GreetingController(
    private val greetingQueryService: GreetingQueryService,
    private val saveGreetingService: SaveGreetingService,
    private val updateGreetingService: UpdateGreetingService,
    private val deleteGreetingService: DeleteGreetingService
) {

    @GetMapping("/greetings")
    fun getGreetings(): ResponseEntity<GreetingResult> {
        try {
            val result = greetingQueryService.get()
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.status(500).build()
        }
    }

    @GetMapping("/greetings/{id}")
    fun getGreetingById(@PathVariable id: UUID): ResponseEntity<GreetingDto> {
        try {
            val result = greetingQueryService.getBy(id)
            return if (result != null) {
                ResponseEntity.ok(result)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            return ResponseEntity.status(500).build()
        }
    }

    @PostMapping("/greetings")
    fun postGreeting(@RequestBody greetingRequest: GreetingPostRequest): ResponseEntity<Greeting> {
        try {
            val result = saveGreetingService.save(greetingRequest.message)
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.status(500).build()
        }
    }

    @PutMapping("/greetings/{id}")
    fun putGreeting(@PathVariable id: UUID, @RequestBody greetingRequest: GreetingPutRequest): ResponseEntity<Greeting> {
        try {
            val result = updateGreetingService.update(id, greetingRequest.message)
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.status(500).build()
        }
    }

    @DeleteMapping("/greetings/{id}")
    fun deleteGreeting(@PathVariable id: UUID): ResponseEntity<Unit> {
        try {
            deleteGreetingService.delete(id)
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            return ResponseEntity.status(500).build()
        }
    }
}

data class GreetingPostRequest(val message: String)
data class GreetingPutRequest(val message: String)