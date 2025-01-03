package com.unicorn.api.controller.medicine

import com.fasterxml.jackson.annotation.JsonFormat
import com.unicorn.api.application_service.medicine.DeleteMedicineService
import com.unicorn.api.application_service.medicine.SaveMedicineService
import com.unicorn.api.application_service.medicine.UpdateMedicineService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.medicine.MedicineID
import com.unicorn.api.domain.medicine_reminders.DayOfWeek
import com.unicorn.api.domain.user.UserID
import com.unicorn.api.query_service.medicine.MedicineQueryService
import com.unicorn.api.query_service.user.UserQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalTime
import java.util.*

@RestController
class MedicineController(
    private val medicineQueryService: MedicineQueryService,
    private val userQueryService: UserQueryService,
    private val saveMedicineService: SaveMedicineService,
    private val updateMedicineService: UpdateMedicineService,
    private val deleteMedicineService: DeleteMedicineService,
) {
    @GetMapping("/medicines")
    fun getMedicines(
        @RequestHeader("X-UID") uid: String,
    ): ResponseEntity<*> {
        return try {
            val user = userQueryService.getOrNullBy(uid) ?: return ResponseEntity.status(400).body("User not found")

            val result = medicineQueryService.getMedicines(uid)
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Internal Server Error")
        }
    }

    @GetMapping("/medicines/reminders")
    fun getMedicineReminders(
        @RequestParam reminderTime: LocalTime,
        @RequestParam reminderDayOfWeek: String,
    ): ResponseEntity<*> {
        return try {
            val result =
                medicineQueryService.getMedicineReminders(
                    reminderTime = reminderTime,
                    reminderDayOfWeek = DayOfWeek.valueOf(reminderDayOfWeek),
                )
            ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(ResponseError("Bad Request"))
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Internal Server Error")
        }
    }

    @PostMapping("/medicines")
    fun save(
        @RequestHeader("X-UID") uid: String,
        @RequestBody medicinePostRequest: MedicinePostRequest,
    ): ResponseEntity<*> {
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
        @PathVariable medicineID: UUID,
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
    fun delete(
        @RequestHeader("X-UID") uid: String,
        @PathVariable medicineID: UUID,
    ): ResponseEntity<Any> {
        try {
            deleteMedicineService.delete(UserID(uid), MedicineID(medicineID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(400).body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.status(500).body(ResponseError("Internal Server Error"))
        }
    }

    @DeleteMapping("/users/{userID}/medicines")
    fun deleteByUserID(
        @RequestHeader("X-UID") uid: String,
        @PathVariable userID: String,
    ): ResponseEntity<Any> {
        try {
            deleteMedicineService.deleteByUserID(UserID(userID))
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
    val count: Int,
    val quantity: Int,
    val dosage: Int,
    val reminders: List<ReminderRequest>,
)

data class MedicinePutRequest(
    val medicineName: String,
    val count: Int,
    val quantity: Int,
    val dosage: Int,
    val reminders: List<ReminderRequest>,
)

data class ReminderRequest(
    val reminderID: UUID,
    @JsonFormat(pattern = "HH:mm")
    val reminderTime: LocalTime,
    val reminderDayOfWeek: List<String>,
)
