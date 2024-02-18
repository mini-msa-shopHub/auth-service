package com.example.authservice.feign

import com.example.authservice.dto.EmailDto
import com.example.authservice.dto.UpdateRefreshRequest
import com.example.authservice.dto.UserInfo
import com.example.authservice.util.CommonResponse
import com.example.authservice.util.EmptyDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping

@FeignClient(name = "auth-service", url = "http://localhost:8000/api/v1/users")
@Qualifier(value = "auth-service")
interface UserFeignClient {

    @GetMapping("/user-info")
    fun getUserInfo(
        emailDto: EmailDto
    ): CommonResponse<UserInfo>

    @PatchMapping("/refresh-token")
    fun updateRefreshToken(
        updateRefreshRequest: UpdateRefreshRequest
    ): CommonResponse<EmptyDto>
}