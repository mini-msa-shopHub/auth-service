package com.example.authservice.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val userRole: String
)