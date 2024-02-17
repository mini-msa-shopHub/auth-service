package com.example.authservice

import com.example.authservice.service.JwtService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController(
    private val jwtService: JwtService,
    private val env: Environment
) {

    @GetMapping("/api/v1/auth/health")
    fun health(request: HttpServletRequest, response: HttpServletResponse): String {
        println(request.getHeader("email"))
        return "Welcome to the AuthService!"
    }

}