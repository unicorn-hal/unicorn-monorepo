package com.unicorn.api.controller.app_config

import com.unicorn.api.query_service.app_config.AppConfigQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/app_config")
class AppConfigController(
    private val appConfigQueryService: AppConfigQueryService
) {
    @GetMapping
    fun getAppConfig(): ResponseEntity<Any> {
        return try {
            val response = appConfigQueryService.get()
            response.available?.let { 
                ResponseEntity.ok(AvailableResponse(it)) 
            } ?: ResponseEntity.status(500).body(ErrorResponse(response.errorType ?: "serverError"))
        } catch (e: Exception) {
            ResponseEntity.status(500).build()
        }
    }

    data class ErrorResponse(val errorType: String)
    data class AvailableResponse(val available: Boolean)
}
