package com.unicorn.api.controller.robot

import com.unicorn.api.application_service.robot.SaveRobotService
import com.unicorn.api.controller.api_response.ResponseError
import com.unicorn.api.domain.account.UID
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@Controller
class RobotController(
    val robotService: SaveRobotService,
) {
    @PostMapping("/robot")
    fun save(
        @RequestHeader("X-UID") uid: String,
        @RequestBody saveRobotRequest: SaveRobotRequest,
    ): ResponseEntity<*> {
        try {
            val result = robotService.save(UID(uid), saveRobotRequest)
            return ResponseEntity.ok(result)
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
