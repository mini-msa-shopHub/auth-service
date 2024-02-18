package com.example.authservice.dto

import com.example.authservice.passport.PassportDto

data class EmailDto(
    val value: String,
    val passportDto: PassportDto
)
