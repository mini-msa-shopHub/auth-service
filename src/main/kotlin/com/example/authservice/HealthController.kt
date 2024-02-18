package com.example.authservice

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @GetMapping("/api/v1/auth/health")
    fun health(request: HttpServletRequest, response: HttpServletResponse): String {
        println(request.getHeader("email"))
        return "Welcome to the AuthService!"
    }

}