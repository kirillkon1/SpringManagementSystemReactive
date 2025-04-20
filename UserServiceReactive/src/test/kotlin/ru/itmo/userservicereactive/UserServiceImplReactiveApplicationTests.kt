package ru.itmo.userservicereactive.service.impl

import io.jsonwebtoken.JwtException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import ru.itmo.userservicereactive.dto.RegisterRequest
import ru.itmo.userservicereactive.model.User
import ru.itmo.userservicereactive.repository.UserRepository
import ru.itmo.userservicereactive.security.JwtTokenProvider
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class AuthServiceImplTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @Mock
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @InjectMocks
    private lateinit var authService: AuthServiceImpl

    private val registerRequest =
        RegisterRequest(
            username = "user1",
            password = "pass123",
            email = "user1@example.com",
            firstName = "First",
            lastName = "Last",
        )

    private val now = LocalDateTime.of(2025, 4, 20, 12, 0)
    private val savedUser =
        User(
            id = 1L,
            username = "user1",
            password = "encodedPass",
            email = "user1@example.com",
            firstName = "First",
            lastName = "Last",
            createdAt = now,
            updatedAt = now,
        )

    @Test
    fun `registerUser should error when username exists`() {
        doReturn(Mono.just(true)).`when`(userRepository).existsByUsername("user1")

        StepVerifier.create(authService.registerUser(registerRequest))
            .expectErrorMessage("Имя пользователя уже занято")
            .verify()

        verify(userRepository, times(1)).existsByUsername("user1")
    }

    @Test
    fun `registerUser should error when email exists`() {
        doReturn(Mono.just(false)).`when`(userRepository).existsByUsername("user1")
        doReturn(Mono.just(true)).`when`(userRepository).existsByEmail("user1@example.com")

        StepVerifier.create(authService.registerUser(registerRequest))
            .expectErrorMessage("Электронная почта уже используется")
            .verify()

        verify(userRepository, times(1)).existsByUsername("user1")
        verify(userRepository, times(1)).existsByEmail("user1@example.com")
    }

    @Test
    fun `registerUser should save and return UserDTO on success`() {
        doReturn(Mono.just(false)).`when`(userRepository).existsByUsername("user1")
        doReturn(Mono.just(false)).`when`(userRepository).existsByEmail("user1@example.com")
        doReturn("encodedPass").`when`(passwordEncoder).encode("pass123")
        doReturn(Mono.just(savedUser)).`when`(userRepository).save(any())

        StepVerifier.create(authService.registerUser(registerRequest))
            .expectNextMatches { dto ->
                dto.id == 1L && dto.username == "user1" && dto.firstName == "First" && dto.lastName == "Last"
            }
            .verifyComplete()

        verify(userRepository, times(1)).existsByUsername("user1")
        verify(userRepository, times(1)).existsByEmail("user1@example.com")
        verify(passwordEncoder, times(1)).encode("pass123")
        verify(userRepository, times(1)).save(any())
    }

    @Test
    fun `authenticateUser should error when user not found`() {
        doReturn(Mono.empty<User>()).`when`(userRepository).findByUsername("unknown")

        StepVerifier.create(authService.authenticateUser("unknown", "pass"))
            .expectErrorMessage("Пользователь unknown не найден")
            .verify()
    }

    @Test
    fun `verifyToken should error on invalid token`() {
        doReturn(Mono.just(false)).`when`(jwtTokenProvider).validateToken("bad-token")

        StepVerifier.create(authService.verifyToken("bad-token"))
            .expectError(JwtException::class.java)
            .verify()
    }

    @Test
    fun `verifyToken should return VerifyTokenDto on valid token`() {
        doReturn(Mono.just(true)).`when`(jwtTokenProvider).validateToken("good-token")
        doReturn(Mono.just("user1")).`when`(jwtTokenProvider).getUsernameFromToken("good-token")

        StepVerifier.create(authService.verifyToken("good-token"))
            .expectNextMatches { it.token == "good-token" && it.verifyStatus && it.userName == "user1" }
            .verifyComplete()
    }
}

@ExtendWith(MockitoExtension::class)
class UserServiceImplTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserServiceImpl

    private val sampleUser =
        User(
            id = 1L,
            username = "user1",
            password = "pass",
            email = "user1@example.com",
            firstName = "First",
            lastName = "Last",
            createdAt = LocalDateTime.of(2025, 4, 20, 12, 0),
            updatedAt = LocalDateTime.of(2025, 4, 20, 12, 0),
        )

    @Test
    fun `getUserById should return user when found`() {
        doReturn(Mono.just(sampleUser)).`when`(userRepository).findById(1L)

        StepVerifier.create(userService.getUserById(1L))
            .expectNext(sampleUser)
            .verifyComplete()

        verify(userRepository, times(1)).findById(1L)
    }

    @Test
    fun `getUserById should error when not found`() {
        doReturn(Mono.empty<User>()).`when`(userRepository).findById(2L)

        StepVerifier.create(userService.getUserById(2L))
            .expectErrorMessage("Пользователь с id 2 не найден")
            .verify()
    }

    @Test
    fun `getAllUsers should return all users`() {
        doReturn(Flux.just(sampleUser)).`when`(userRepository).findAll()

        StepVerifier.create(userService.getAllUsers())
            .expectNext(sampleUser)
            .verifyComplete()

        verify(userRepository, times(1)).findAll()
    }
}
