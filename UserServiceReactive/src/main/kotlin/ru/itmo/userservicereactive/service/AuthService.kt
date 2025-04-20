package ru.itmo.userservicereactive.service

import reactor.core.publisher.Mono
import ru.itmo.userservicereactive.dto.RegisterRequest
import ru.itmo.userservicereactive.dto.UserDTO
import ru.itmo.userservicereactive.dto.VerifyTokenDto

interface AuthService {
    fun registerUser(registerRequest: RegisterRequest): Mono<UserDTO>

    fun authenticateUser(username: String, password: String): Mono<String> // Возвращает JWT

    fun verifyToken(token: String): Mono<VerifyTokenDto>
}
