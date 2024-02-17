package com.example.authservice.controller

import com.example.authservice.dto.LoginRequest
import com.example.authservice.dto.PassportDto
import com.example.authservice.dto.PasswordDto
import com.example.authservice.dto.TokenResponse
import com.example.authservice.service.JwtService
import com.example.authservice.service.LoginService
import com.example.authservice.util.CommonResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val loginService: LoginService,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): CommonResponse<TokenResponse> {
        val authenticationToken = loginRequest.toAuthenticationToken()
        val authentication = authenticationManager.authenticate(authenticationToken)
        println(authentication.name)
        return CommonResponse(jwtService.makeTokenResponse(authentication.name))
    }

    @GetMapping("/encode-password")
    fun encodePassword(@RequestBody passwordDto: PasswordDto): CommonResponse<PasswordDto> {
        return CommonResponse(PasswordDto(
            passwordEncoder.encode(passwordDto.value)
        ))
    }

    @GetMapping("/authentication/{token}")
    fun getAuthentication(@PathVariable("token") token: String): Authentication? {
        val email = jwtService.extractEmail(token) ?: return null
        val userDetails = loginService.loadUserByUsername(email)
        return UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            NullAuthoritiesMapper().mapAuthorities(userDetails.authorities)
        )
    }

    @GetMapping("/check-token/{token}")
    fun checkToken(@PathVariable("token") token: String): Boolean {
        return jwtService.isTokenValid(token)
    }

    @GetMapping("/passport")
    fun checkPassport(@RequestBody passportDto: PassportDto): CommonResponse<Boolean> {
        if (passportDto.email == jwtService.decodeToken(passportDto.token)) {
            return CommonResponse(true)
        }
        return CommonResponse(false)
    }

}