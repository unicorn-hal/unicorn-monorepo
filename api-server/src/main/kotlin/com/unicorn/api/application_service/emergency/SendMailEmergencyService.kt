package com.unicorn.api.application_service.emergency

import com.unicorn.api.domain.emergency.Emergency
import com.unicorn.api.infrastructure.doctor.DoctorRepository
import com.unicorn.api.infrastructure.family_email.FamilyEmailRepository
import com.unicorn.api.infrastructure.primary_doctor.PrimaryDoctorRepository
import com.unicorn.api.infrastructure.user.UserRepository
import com.unicorn.api.util.MailTransport
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

interface SendMailEmergencyService {
    fun send(emergency: Emergency)
}

@Service
class SendMailEmergencyServiceImpl(
    private val userRepository: UserRepository,
    private val familyEmailRepository: FamilyEmailRepository,
    private val primaryDoctorRepository: PrimaryDoctorRepository,
    private val doctorRepository: DoctorRepository,
    private val mailTransport: MailTransport,
) : SendMailEmergencyService {
    @Async
    override fun send(emergency: Emergency) {
        val user = userRepository.getOrNullBy(emergency.userID) ?: return

        val familyEmails = familyEmailRepository.getOrNullByUserID(emergency.userID)

        val primaryDoctors = primaryDoctorRepository.getOrNullByUserID(emergency.userID)

        if (familyEmails == null && primaryDoctors == null) return

        val subject = "${user.lastName.value} ${user.firstName.value}さんの緊急要請"

        val body =
            //language=html
            """
            <html>
            <head>
            </head>
            <body>
            <h1>${user.lastName.value} ${user.firstName.value}さんの緊急要請</h1>
            <p>${user.lastName.value} ${user.firstName.value}さんがunicornを緊急要請しました。</p>
            </body>
            </html>
            """

        if (familyEmails != null) {
            familyEmails.map {
                val email = it.email.value
                mailTransport.send(email, subject, body)
            }
        }

        if (primaryDoctors != null) {
            primaryDoctors.map {
                val doctor = doctorRepository.getOrNullBy(it.doctorID) ?: return
                val email = doctor.email.value
                mailTransport.send(email, subject, body)
            }
        }
    }
}
