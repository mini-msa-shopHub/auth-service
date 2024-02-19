package com.example.authservice.controller

import com.example.authservice.dto.EmailDto
import com.example.authservice.dto.PasswordDto
import com.example.authservice.passport.IntegrityEncoder
import com.example.authservice.passport.PassportDto
import com.example.authservice.service.JwtService
import com.example.authservice.util.CommonResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthInternalController(
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    @GetMapping("/encode-password")
    fun encodePassword(
        @RequestBody passwordDto: PasswordDto,
    ): CommonResponse<PasswordDto> {
        checkPassport(passwordDto.passportDto)
        return CommonResponse(PasswordDto(passwordEncoder.encode(passwordDto.value), passwordDto.passportDto))
    }

    @GetMapping("/{token}")
    fun decode(@PathVariable("token") token: String): EmailDto {
        println("통과")
        return EmailDto(jwtService.extractEmail(token)!!)
    }

    private fun checkPassport(passportDto: PassportDto) {
        return IntegrityEncoder.check(passportDto)
    }
}