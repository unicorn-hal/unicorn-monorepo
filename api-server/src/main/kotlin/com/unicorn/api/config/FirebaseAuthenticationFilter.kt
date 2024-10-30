package com.unicorn.api.config

import com.google.firebase.auth.FirebaseAuthException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlin.jvm.Throws

@Component
class FirebaseAuthenticationFilter(
    private val firebaseClient: FirebaseClient,
    @Value("\${filter.firebase-authentication-enabled}") private val isEnabled: Boolean
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (!isEnabled) {
            filterChain.doFilter(request, response)
            return
        }

        val tokenHeader = request.getHeader("Authorization")
            ?: return response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")

        val uid = request.getHeader("X-UID")
        try {
            val token = tokenHeader.split(" ").last()
            val firebaseToken = firebaseClient.verify(token)
            if (uid != null && firebaseToken.uid != uid) {
                return response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
            }
            request.setAttribute("firebaseToken", firebaseToken)
        } catch (e: FirebaseAuthException) {
            return response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        }
        filterChain.doFilter(request, response)
    }
}
