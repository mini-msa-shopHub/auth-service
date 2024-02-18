package com.example.authservice.service

import com.example.authservice.dto.EmailDto
import com.example.authservice.feign.UserFeignClient
import com.example.authservice.passport.IntegrityEncoder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val userFeignClient: UserFeignClient
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {

        val userInfo = userFeignClient.getUserInfo(EmailDto(email, IntegrityEncoder.makePassport(email))).result

        return User.builder()
            .username(email)
            .password(userInfo.password)
            .roles(userInfo.userRole)
            .build()
    }

}