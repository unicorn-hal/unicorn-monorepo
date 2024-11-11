package com.unicorn.api.application_service.health_checkup

import com.unicorn.api.query_service.health_checkup.HealthCheckupDto
import com.unicorn.api.query_service.primary_doctor.PrimaryDoctorQueryService
import com.unicorn.api.query_service.user.UserQueryService
import com.unicorn.api.util.MailTransport
import org.springframework.stereotype.Service

interface SendMailHealthCheckupService {
    fun convertAndSend(healthCheckup: HealthCheckupDto)
}

@Service
class SendMailHealthCheckupServiceImpl(
    private val mailTransport: MailTransport,
    private val userQueryService: UserQueryService,
    private val primaryDoctorQueryService: PrimaryDoctorQueryService,
) : SendMailHealthCheckupService {
    override fun convertAndSend(healthCheckup: HealthCheckupDto) {
        val user =
            userQueryService.getOrNullBy(healthCheckup.userID)
                ?: return
        val primaryDoctors = primaryDoctorQueryService.getBy(healthCheckup.userID)
        primaryDoctors.data.map {
            val primaryDoctorEmails = it.email
            val subject = "${user.firstName} ${user.lastName}の検査結果"
            val body =
                //language=html
                """
                <html>
                <body>
                <h1>${user.firstName} ${user.lastName}の検査結果</h1>
                <p>患者様の検査結果をお知らせします。</p>
                <p>${healthCheckup.medicalRecord}</p>
                <botton><a href="#">検査結果を確認する</a></button>
                </body>
                </html>
                """
            mailTransport.send(primaryDoctorEmails, subject, body)
        }
    }
}
