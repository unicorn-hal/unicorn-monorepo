package com.unicorn.api.config

import com.google.firebase.auth.FirebaseAuthException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import kotlin.jvm.Throws

@Component
@ConfigurationProperties(prefix = "filter")
data class FilterProperties(
    var firebaseAuthenticationEnabled: Boolean = false,
)

@Component
class FirebaseAuthenticationFilter(
    private val firebaseClient: FirebaseClient,
    private val filterProperties: FilterProperties,
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (!filterProperties.firebaseAuthenticationEnabled) {
            filterChain.doFilter(request, response)
            return
        }

        if (request.requestURI.startsWith("/ws")) {
            filterChain.doFilter(request, response)
            return
        }

        if (request.requestURI.startsWith("/app_config")) {
            filterChain.doFilter(request, response)
            return
        }

        val tokenHeader =
            request.getHeader("Authorization")
                ?: return response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")

        try {
            val token = tokenHeader.split(" ").last()
            val firebaseToken = firebaseClient.verify(token)
            request.setAttribute("firebaseToken", firebaseToken)
        } catch (e: FirebaseAuthException) {
            return response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        }
        filterChain.doFilter(request, response)
    }
}
