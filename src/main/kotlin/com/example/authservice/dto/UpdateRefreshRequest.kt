package com.example.authservice.dto

data class UpdateRefreshRequest(
    val email: String,
    val refreshToken: String
)
