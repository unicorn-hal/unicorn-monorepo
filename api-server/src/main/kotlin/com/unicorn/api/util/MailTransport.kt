package com.unicorn.api.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

interface MailTransport {
    fun send(
        toAddress: String,
        subject: String,
        body: String,
    )
}

@Component
class MailTransportImpl(
    @Value("\${spring.mail.host}")
    private val smtpHost: String,
    @Value("\${spring.mail.port}")
    private val smtpPort: Int,
    @Value("\${spring.mail.username}")
    private val fromAddress: String,
    @Value("\${spring.mail.password}")
    private val password: String,
) : MailTransport {
    override fun send(
        toAddress: String,
        subject: String,
        body: String,
    ) {
        val props = Properties()
        props["mail.smtp.host"] = smtpHost
        props["mail.smtp.port"] = smtpPort.toString()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"

        val session =
            Session.getInstance(
                props,
                object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(fromAddress, password)
                    }
                },
            )
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(fromAddress))
        message.setSubject(subject)
        message.setContent(body, "text/html; charset=UTF-8")
        message.setRecipient(Message.RecipientType.TO, InternetAddress(toAddress))
        Transport.send(message)
    }
}
