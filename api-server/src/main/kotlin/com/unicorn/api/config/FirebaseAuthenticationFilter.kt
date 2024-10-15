package com.unicorn.api.config

import com.google.firebase.auth.FirebaseAuthException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import kotlin.jvm.Throws

@Component
@Profile("prod")
class FirebaseAuthenticationFilter(
   private val firebaseClient: FirebaseClient
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val tokenHeader = request.getHeader("Authorization")
        val uid = request.getHeader("X-UID")
        if (tokenHeader != null) {
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
        }
        filterChain.doFilter(request, response)
    }
}