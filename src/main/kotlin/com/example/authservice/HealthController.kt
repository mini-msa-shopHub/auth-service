package com.example.authservice

import com.example.authservice.passport.IntegrityEncoder
import com.example.authservice.passport.PassportDto
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @GetMapping("/api/v1/auth/health")
    fun health(request: HttpServletRequest, response: HttpServletResponse): String {
        val email = request.getHeader("email")
        val token = request.getHeader("passportToken")
        IntegrityEncoder.check(PassportDto(email, token))
        return "Welcome to the AuthService!"
    }

}