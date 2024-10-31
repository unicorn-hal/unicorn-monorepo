package com.unicorn.api.controller.departments

import com.unicorn.api.query_service.departments.DepartmentsQueryService
import com.unicorn.api.query_service.departments.DepartmentsResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class DepartmentsController(
    private val departmentsQueryService: DepartmentsQueryService,
) {
    @GetMapping("/departments")
    fun getDepartments(
        @RequestHeader("X-UID") uid: String,
    ): ResponseEntity<DepartmentsResult> {
        try {
            val result = departmentsQueryService.get()
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return ResponseEntity.status(500).build()
        }
    }
}
