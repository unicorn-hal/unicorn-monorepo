package com.unicorn.api.controller.medicine

import com.unicorn.api.application_service.medicine.DeleteMedicineService
import com.unicorn.api.application_service.medicine.SaveMedicineService
import com.unicorn.api.application_service.medicine.UpdateMedicineService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.medicine.MedicineID
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
    private val saveMedicineService: SaveMedicineService,
    private val updateMedicineService: UpdateMedicineService,
    private val deleteMedicineService: DeleteMedicineService
) {
    @GetMapping("/medicines")
    fun getMedicines(@RequestHeader("X-UID") uid: String): ResponseEntity<*> {
        return try {
            val user = userQueryService.getOrNullBy(uid)

            if (user == null) {
                return ResponseEntity.status(400).body("User not found")
            }

            val result = medicineQueryService.getMedicines(uid)
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Internal Server Error")
        }
    }

    @PostMapping("/medicines")
    fun save(@RequestHeader("X-UID") uid: String, @RequestBody medicinePostRequest: MedicinePostRequest): ResponseEntity<*> {
        try {

            val result = saveMedicineService.save(UID(uid), medicinePostRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @PutMapping("/medicines/{medicineID}")
    fun update(
        @RequestHeader("X-UID") uid: String,
        @RequestBody medicinePutRequest: MedicinePutRequest,
        @PathVariable medicineID: UUID
    ): ResponseEntity<*> {
        try {
            val result = updateMedicineService.update(MedicineID(medicineID), UserID(uid), medicinePutRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/medicines/{medicineID}")
    fun delete(@RequestHeader("X-UID") uid: String, @PathVariable medicineID: UUID): ResponseEntity<Any> {
        try {
            deleteMedicineService.delete(UserID(uid), MedicineID(medicineID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }
}

data class MedicinePostRequest(
    val medicineName: String,
    val count: Int
)

data class MedicinePutRequest(
    val medicineName: String,
    val quantity: Int
)

