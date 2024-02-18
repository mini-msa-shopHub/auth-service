package com.example.authservice.dto

import com.example.authservice.passport.PassportDto

data class UpdateRefreshRequest(
    val email: String,
    val refreshToken: String,
    val passportDto: PassportDto
)
