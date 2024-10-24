package com.unicorn.api.controller.medicine

import com.unicorn.api.query_service.medicine.MedicineQueryService
import com.unicorn.api.query_service.medicine.MedicineResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class MedicineController(
    private val medicineQueryService: MedicineQueryService
) {
    @GetMapping("/medicines")
    fun getMedicines(@RequestHeader("X-UID") uid: String): ResponseEntity<MedicineResult> {
        return try {
            val result = medicineQueryService.getMedicines()
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            ResponseEntity.status(500).build()
        }
    }
}