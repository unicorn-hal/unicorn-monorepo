package com.unicorn.api.controller.medicine

import com.unicorn.api.application_service.medicine.SaveMedicineService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.controller.user.UserPutRequest
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.query_service.medicine.MedicineQueryService
import com.unicorn.api.query_service.user.UserQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class MedicineController(
    private val medicineQueryService: MedicineQueryService,
    private val userQueryService: UserQueryService,
    private val saveMedicineService: SaveMedicineService
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

    @PostMapping("/medicines")
    fun save(@RequestHeader("X-UID") uid: String, @RequestBody medicinePostRequest: MedicinePostRequest): ResponseEntity<*> {
        try {
            val user = userQueryService.getOrNullBy(uid)
                ?: return ResponseEntity.status(400).body(ResponseError("User not found"))

            val medicinePostData = MedicinePostData(
                medicineName = medicinePostRequest.medicineName,
                count = medicinePostRequest.count,
                quantity = medicinePostRequest.quantity
            )

            val result = saveMedicineService.save(UID(uid), medicinePostData)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}

data class MedicinePostRequest(
    val medicineName: String,
    val count: Int,
    val quantity: Int
)

data class MedicinePostData(
    val medicineName: String,
    val count: Int,
    val quantity: Int
)

