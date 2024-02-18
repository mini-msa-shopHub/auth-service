package com.example.authservice.controller

import com.example.authservice.dto.LoginRequest
import com.example.authservice.dto.TokenResponse
import com.example.authservice.passport.IntegrityEncoder
import com.example.authservice.passport.PassportDto
import com.example.authservice.service.JwtService
import com.example.authservice.util.CommonResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthServiceController(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest, request: HttpServletRequest): CommonResponse<TokenResponse> {
        val authenticationToken = loginRequest.toAuthenticationToken()
        val authentication = authenticationManager.authenticate(authenticationToken)
        return CommonResponse(jwtService.makeTokenResponse(authentication.name))
    }

    private fun checkGatewayPassport(request: HttpServletRequest) {
        val email = request.getHeader("email")
        val passport = request.getHeader("passportToken")
        IntegrityEncoder.check(PassportDto(email, passport))
    }

}