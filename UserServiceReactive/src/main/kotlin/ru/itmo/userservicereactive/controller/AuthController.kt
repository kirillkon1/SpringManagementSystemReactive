package ru.itmo.userservicereactive.controller

import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.itmo.usermanagementservice.dto.AuthResponse
import ru.itmo.usermanagementservice.dto.LoginRequest
import ru.itmo.usermanagementservice.dto.RegisterRequest
import ru.itmo.usermanagementservice.dto.UserDTO
import ru.itmo.userservicereactive.dto.VerifyTokenDto
import ru.itmo.userservicereactive.service.AuthService

@RestController
@RequestMapping("/api/auth")
@Validated
class AuthController(private val authService: AuthService) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/register")
    fun registerUser(
        @Valid @RequestBody registerRequest: RegisterRequest,
    ): Mono<ResponseEntity<UserDTO>> {
        logger.info("User  ${registerRequest.username} register attempt")
        return authService.registerUser(registerRequest)
            .map { userDTO ->
                ResponseEntity.status(HttpStatus.CREATED).body(userDTO)
            }
    }

    @PostMapping("/login")
    fun loginUser(
        @Valid @RequestBody loginRequest: LoginRequest,
    ): Mono<ResponseEntity<AuthResponse>> {
        logger.info("Logging in user ${loginRequest.username}")
        return authService.authenticateUser(loginRequest.username, loginRequest.password)
            .map { token ->
                ResponseEntity.ok(AuthResponse(token = token, username = loginRequest.username))
            }
    }

    @PostMapping("/verify")
    fun verifyToken(
        @Valid @RequestBody token: String,
    ): Mono<ResponseEntity<VerifyTokenDto>> {
        return authService.verifyToken(token)
            .map { verifyDto ->
                ResponseEntity.ok(verifyDto)
            }
    }
}
