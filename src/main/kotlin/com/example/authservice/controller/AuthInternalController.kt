package com.example.authservice.controller

import com.example.authservice.dto.PasswordDto
import com.example.authservice.passport.IntegrityEncoder
import com.example.authservice.passport.PassportDto
import com.example.authservice.util.CommonResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthInternalController(
    private val passwordEncoder: PasswordEncoder
) {

    @GetMapping("/encode-password")
    fun encodePassword(
        @RequestBody passwordDto: PasswordDto,
    ): CommonResponse<PasswordDto> {
        checkPassport(passwordDto.passportDto)
        return CommonResponse(PasswordDto(passwordEncoder.encode(passwordDto.value), passwordDto.passportDto))
    }

    private fun checkPassport(passportDto: PassportDto) {
        return IntegrityEncoder.check(passportDto)
    }
}