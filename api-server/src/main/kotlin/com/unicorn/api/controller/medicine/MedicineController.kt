package com.unicorn.api.controller.medicine

import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.controller.user.UserPostRequest
import com.unicorn.api.domain.account.UID
import com.unicorn.api.query_service.medicine.MedicineQueryService
import com.unicorn.api.query_service.medicine.MedicineResult
import com.unicorn.api.query_service.user.UserQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class MedicineController(
    private val medicineQueryService: MedicineQueryService,
    private val userQueryService: UserQueryService
) {
    @GetMapping("/medicines")
    fun getMedicines(@RequestHeader("X-UID") uid: String): ResponseEntity<*> {
        return try {
            val user = userQueryService.getOrNullBy(uid)

            if (user != null) {
                val result = medicineQueryService.getMedicines(uid)
                ResponseEntity.ok(result)
            } else {
                ResponseEntity.status(400).body("User not found")
            }
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Internal Server Error")
        }
    }
}