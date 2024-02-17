package com.example.authservice.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.example.authservice.dto.TokenResponse
import com.example.authservice.dto.UpdateRefreshRequest
import com.example.authservice.feign.UserFeignClient
import jakarta.servlet.http.HttpServletRequest
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper
import org.springframework.stereotype.Service
import java.util.*

/**
 * jwt 생성, 전송, 처리 서비스
 */
@Service
class JwtService(
    @Value("\${jwt.secretKey}")
    private var secretKey: String,

    @Value("\${jwt.access.expiration}")
    private val accessTokenExpirationPeriod: Long,

    @Value("\${jwt.refresh.expiration}")
    private val refreshTokenExpirationPeriod: Long,

    private val userFeignClient: UserFeignClient,
) {

    companion object {
        const val ACCESS_TOKEN_SUBJECT: String = "AccessToken"
        const val REFRESH_TOKEN_SUBJECT: String = "RefreshToken"
        const val SIGN_UP_SUBJECT: String = "SignUp"
        const val EMAIL_CLAIM: String = "email"
        const val BEARER: String = "Bearer "
        const val JWT_TOKEN: String = "Authorization"
        const val TEN_MINUTE = 3600000

        private val logger = LoggerFactory.getLogger(JwtService::class.java)
    }

    fun makeTokenResponse(email: String): TokenResponse {
        val accessToken = createAccessToken(email)
        val refreshToken = createRefreshToken()
        userFeignClient.updateRefreshToken(UpdateRefreshRequest(
            email = email,
            refreshToken = refreshToken
        ))
        return TokenResponse(accessToken, refreshToken, "")
    }

    private fun createAccessToken(email: String): String {
        val now = Date()
        return JWT.create()
            .withIssuer("admin")
            .withSubject(ACCESS_TOKEN_SUBJECT)
            .withExpiresAt(Date(now.time + accessTokenExpirationPeriod))
            .withClaim(EMAIL_CLAIM, email)
            .sign(Algorithm.HMAC512(secretKey))
    }

    private fun createRefreshToken(): String {
        val now = Date()
        return JWT.create()
            .withSubject(REFRESH_TOKEN_SUBJECT)
            .withExpiresAt(Date(now.time + refreshTokenExpirationPeriod))
            .sign(Algorithm.HMAC512(secretKey))
    }

    fun createTokenForOAuth2(email: String): String {
        val now = Date()
        return JWT.create()
            .withSubject(SIGN_UP_SUBJECT)
            .withExpiresAt(Date(now.time + TEN_MINUTE))
            .withClaim(EMAIL_CLAIM, email)
            .sign(Algorithm.HMAC512(secretKey))
    }

    fun extractToken(request: HttpServletRequest): String? {
        val header = request.getHeader(JWT_TOKEN) ?: return null
        return if (header.startsWith(BEARER)) {
            header.replace(BEARER, "")
        } else null
    }

    fun extractEmail(token: String?): String? {
        return try {
            JWT.require(Algorithm.HMAC512(secretKey))
                .withIssuer("admin")
                .build()
                .verify(token)
                .getClaim(EMAIL_CLAIM)
                .asString()
        } catch (e: Exception) {
            println(e.message)
            println(e.cause)
            null
        }
    }

    fun isTokenValid(token: String?): Boolean {
        if (token == null) {
            return false
        }
        return try {
            JWT.require(Algorithm.HMAC512(secretKey))
                .build().verify(token)
            true
        } catch (e: TokenExpiredException) {
            logger.info("토큰 만료")
            false
        } catch (e: AlgorithmMismatchException) {
            logger.info("알고리즘 불일치")
            false
        } catch (e: SignatureVerificationException) {
            logger.info("서명 불일치")
            false
        } catch (e: JWTVerificationException) {
            logger.info("토큰 문제")
            false
        }
    }

    fun decodeToken(token: String): String? {
        return try {
            JWT.require(Algorithm.HMAC512(secretKey))
                .withIssuer("admin")
                .build()
                .verify(token)
                .getClaim(EMAIL_CLAIM)
                .asString()
        } catch (e: Exception) {
            null
        }
    }

}