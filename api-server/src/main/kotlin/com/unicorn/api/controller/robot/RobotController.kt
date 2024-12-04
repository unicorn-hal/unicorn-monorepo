package com.unicorn.api.controller.robot

import com.unicorn.api.application_service.robot.DeleteRobotService
import com.unicorn.api.application_service.robot.SaveRobotService
import com.unicorn.api.application_service.robot.UpdateRobotService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.account.UID
import com.unicorn.api.domain.robot.RobotID
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class RobotController(
    private val saveRobotService: SaveRobotService,
    private val updateRobotService: UpdateRobotService,
    private val deleteRobotService: DeleteRobotService,
) {
    @PostMapping("/robots")
    fun save(
        @RequestHeader("X-UID") uid: String,
        @RequestBody saveRobotRequest: SaveRobotRequest,
    ): ResponseEntity<*> {
        try {
            val result = saveRobotService.save(UID(uid), saveRobotRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body("Internal Server Error")
        }
    }

    @PutMapping("/robots/{robotID}")
    fun update(
        @RequestHeader("X-UID") uid: String,
        @PathVariable robotID: String,
        @RequestBody updateRobotRequest: UpdateRobotRequest,
    ): ResponseEntity<*> {
        try {
            val result = updateRobotService.update(RobotID(robotID), updateRobotRequest)
            return ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body("Internal Server Error")
        }
    }

    @DeleteMapping("/robots/{robotID}")
    fun delete(
        @RequestHeader("X-UID") uid: String,
        @PathVariable robotID: String,
    ): ResponseEntity<Any> {
        try {
            deleteRobotService.delete(RobotID(robotID))
            return ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ResponseError(e.message ?: "Bad Request"))
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().body("Internal Server Error")
        }
    }
}

data class SaveRobotRequest(
    val robotName: String,
)

data class UpdateRobotRequest(
    val robotName: String,
)
