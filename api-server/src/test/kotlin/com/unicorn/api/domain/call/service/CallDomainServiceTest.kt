package com.unicorn.api.domain.call.service

import com.unicorn.api.controller.call.CallPostRequest
import com.unicorn.api.domain.call_support.CallSupport
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.Test

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class CallDomainServiceTest {
    private val callDomainService = CallDomainService()

    private val doctorCallSupport =
        CallSupport.fromStore(
            callSupportID = UUID.fromString("d1b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b2b"),
            doctorID = "doctor",
            callSupportStartHour = LocalTime.of(9, 0),
            callSupportEndHour = LocalTime.of(18, 0),
        )

    @Test
    fun `should not throw exception when call times are within doctor's support hours`() {
        val callPostRequest =
            CallPostRequest(
                userID = "test",
                doctorID = "doctor",
                callStartTime = OffsetDateTime.parse("2025-10-12T15:00:00+09:00"),
                callEndTime = OffsetDateTime.parse("2025-10-12T15:30:00+09:00"),
            )

        // サポート時間内なので、例外はスローされない
        assertDoesNotThrow {
            callDomainService.validateCallTimes(callPostRequest, doctorCallSupport)
        }
    }

    @Test
    fun `should throw exception when call times do not match doctor's support hours`() {
        val callPostRequest =
            CallPostRequest(
                userID = "test",
                doctorID = "doctor",
                callStartTime = OffsetDateTime.parse("2024-10-12T08:00:00+09:00"),
                callEndTime = OffsetDateTime.parse("2024-10-12T08:30:00+09:00"),
            )

        // サポート時間外なので、例外がスローされる
        assertThrows(IllegalArgumentException::class.java) {
            callDomainService.validateCallTimes(callPostRequest, doctorCallSupport)
        }
    }
}
