package com.unicorn.api.controller.greeting

import com.unicorn.api.query_service.GreetingDto
import com.unicorn.api.query_service.GreetingQueryService
import com.unicorn.api.query_service.GreetingResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class GreetingController(val greetingQueryService: GreetingQueryService) {

    @GetMapping("/greetings")
    fun hello(): ResponseEntity<GreetingResult> {
        try {
            val result = greetingQueryService.get()
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.status(500).build()
        }
    }

    @GetMapping("/greetings/{id}")
    fun hello(@PathVariable id: UUID): ResponseEntity<GreetingDto> {
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
}