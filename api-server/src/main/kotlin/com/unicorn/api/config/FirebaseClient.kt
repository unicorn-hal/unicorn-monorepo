package com.unicorn.api.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import com.google.firebase.auth.UserRecord
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream

@Component
class FirebaseClient {
    init {
        try {
            val firebaseCredentialsJson =
                System.getenv("FIREBASE_CREDENTIALS_JSON")
                    ?: throw IllegalArgumentException("FIREBASE_CREDENTIALS_JSON is not set")

            val validJson = firebaseCredentialsJson.trim('\'')
            val parsedJson: JsonObject = JsonParser.parseString(validJson).asJsonObject
            val prettyJson = parsedJson.toString()

            val credentialsStream = ByteArrayInputStream(prettyJson.toByteArray())

            val options =
                FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Firebase initialization failed", e)
        }
    }

    @Throws(FirebaseAuthException::class)
    fun verify(token: String): FirebaseToken {
        return FirebaseAuth.getInstance().verifyIdToken(token)
    }

    @PreDestroy
    fun destroy() {
        FirebaseApp.getInstance()?.delete()
    }

    @Throws(FirebaseAuthException::class)
    fun createAccount(
        email: String,
        password: String,
    ): String {
        val user =
            FirebaseAuth.getInstance().createUser(
                UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password),
            )
        return user.uid
    }
}
