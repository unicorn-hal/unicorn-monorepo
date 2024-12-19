package com.unicorn.api.controller.departments

import com.unicorn.api.application_service.doctor_department.DeleteDoctorDepartmentsService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.doctor.DoctorID
import com.unicorn.api.query_service.departments.DepartmentsQueryService
import com.unicorn.api.query_service.departments.DepartmentsResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class DepartmentsController(
    private val departmentsQueryService: DepartmentsQueryService,
    private val deleteDoctorDepartmentsService: DeleteDoctorDepartmentsService,
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

    @DeleteMapping("/doctors/{doctorID}/doctor_departments")
    fun deleteByDoctorID(
        @RequestHeader("X-UID") uid: String,
        @PathVariable doctorID: String,
    ): ResponseEntity<Any> {
        try {
            deleteDoctorDepartmentsService.deleteByDoctorID(DoctorID(doctorID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}
