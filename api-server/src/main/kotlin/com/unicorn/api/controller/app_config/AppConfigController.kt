package com.unicorn.api.controller.app_config

import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.query_service.app_config.AppConfigQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/app_config")
class AppConfigController(
    private val appConfigQueryService: AppConfigQueryService,
) {
    @GetMapping
    fun getAppConfig(): ResponseEntity<Any> {
        return try {
            val response =
                appConfigQueryService.get()
                    ?: return ResponseEntity.status(500).body(ResponseError("serverError"))

            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.status(500).build()
        }
    }
}
