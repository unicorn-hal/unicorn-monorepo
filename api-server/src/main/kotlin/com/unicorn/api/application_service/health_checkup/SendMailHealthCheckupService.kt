package com.unicorn.api.application_service.health_checkup

import com.unicorn.api.domain.health_checkup.HealthCheckupSavedEvent
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.health_checkup.HealthCheckupRepository
import com.unicorn.api.infrastructure.primary_doctor.PrimaryDoctorRepository
import com.unicorn.api.infrastructure.user.UserRepository
import com.unicorn.api.util.MailTransport
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

interface SendMailHealthCheckupService {
    fun convertAndSend(healthCheckupSavedEvent: HealthCheckupSavedEvent)
}

@Service
class SendMailHealthCheckupServiceImpl(
    private val mailTransport: MailTransport,
    private val healthCheckupRepository: HealthCheckupRepository,
    private val userRepository: UserRepository,
    private val primaryDoctorRepository: PrimaryDoctorRepository,
    private val doctorRepository: DoctorRepository,
) : SendMailHealthCheckupService {
    @Async
    override fun convertAndSend(healthCheckupSavedEvent: HealthCheckupSavedEvent) {
        val healthCheckup =
            healthCheckupRepository.getOrNullBy(healthCheckupSavedEvent.HealthCheckup.healthCheckupID)
                ?: return

        val user =
            userRepository.getOrNullBy(healthCheckup.userID)
                ?: return

        val primaryDoctors =
            primaryDoctorRepository.getOrNullByUserID(healthCheckup.userID)

        primaryDoctors.map {
            val doctor = doctorRepository.getOrNullBy(it.doctorID) ?: return
            val primaryDoctorEmail = doctor.email.value
            val subject = "${user.lastName.value} ${user.firstName.value}さんの検査結果"
            val body =
                //language=html
                """
                <html>
                <head>
                </head>
                <body>
                <h1>${user.lastName.value} ${user.firstName.value}さんの検査結果</h1>
                <p>${user.lastName.value} ${user.firstName.value}さんの検査結果をお知らせします。</p>
                <a href="https://unicorn-doctor-frontend.pages.dev/doctors/patients/${user.userID.value}">検査結果を確認する</a>
                </body>
                </html>
                """
            mailTransport.send(primaryDoctorEmail, subject, body)
        }
    }
}
