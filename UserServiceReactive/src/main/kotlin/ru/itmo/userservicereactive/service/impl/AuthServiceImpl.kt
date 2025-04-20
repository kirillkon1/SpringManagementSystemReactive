package ru.itmo.userservicereactive.service.impl

import io.jsonwebtoken.JwtException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.itmo.userservicereactive.dto.RegisterRequest
import ru.itmo.userservicereactive.dto.UserDTO
import ru.itmo.userservicereactive.dto.VerifyTokenDto
import ru.itmo.userservicereactive.dto.mapToDto
import ru.itmo.userservicereactive.model.User
import ru.itmo.userservicereactive.repository.UserRepository
import ru.itmo.userservicereactive.security.JwtTokenProvider
import ru.itmo.userservicereactive.service.AuthService
import java.time.LocalDateTime

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
) : AuthService {
    override fun registerUser(registerRequest: RegisterRequest): Mono<UserDTO> {
        return userRepository.existsByUsername(registerRequest.username)
            .flatMap { existsByUsername ->
                if (existsByUsername) {
                    Mono.error<UserDTO>(RuntimeException("Имя пользователя уже занято"))
                } else {
                    userRepository.existsByEmail(registerRequest.email)
                        .flatMap { existsByEmail ->
                            if (existsByEmail) {
                                Mono.error<UserDTO>(RuntimeException("Электронная почта уже используется"))
                            } else {
                                val user =
                                    User(
                                        username = registerRequest.username,
                                        password = passwordEncoder.encode(registerRequest.password),
                                        email = registerRequest.email,
                                        firstName = registerRequest.firstName,
                                        lastName = registerRequest.lastName,
                                        updatedAt = LocalDateTime.now(),
                                        createdAt = LocalDateTime.now(),
                                    )
                                userRepository.save(user)
                                    .map { mapToDto(it) }
                            }
                        }
                }
            }
    }

    override fun authenticateUser(
        username: String,
        password: String,
    ): Mono<String> {
        return userRepository.findByUsername(username)
            .switchIfEmpty(Mono.error(RuntimeException("Пользователь $username не найден")))
            .flatMap { user ->
                if (!passwordEncoder.matches(password, user.password)) {
                    Mono.error(RuntimeException("Неверный пароль"))
                } else {
                    jwtTokenProvider.generateToken(user.username)
                }
            }
    }

    override fun verifyToken(token: String): Mono<VerifyTokenDto> {
        return jwtTokenProvider.validateToken(token)
            .flatMap { status ->
                if (!status) {
                    Mono.error(JwtException("Невалидный токен!"))
                } else {
                    // В этом случае можно продолжить формировать ответ
                    Mono.just(
                        VerifyTokenDto(
                            token = token,
                            verifyStatus = true,
                            userName =
                                jwtTokenProvider.getUsernameFromToken(token)
                                    .block(),
                        ),
                    )
                }
            }
    }
}
