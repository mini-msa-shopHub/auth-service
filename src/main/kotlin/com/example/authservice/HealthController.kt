package com.example.authservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @GetMapping("/api/v1/auth/health")
    fun health(): String = "Welcome to the AuthService!"

}