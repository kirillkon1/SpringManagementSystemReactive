package ru.itmo.userservicereactive.service.impl

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.userservicereactive.model.User
import ru.itmo.userservicereactive.repository.UserRepository
import ru.itmo.userservicereactive.service.UserService

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun getUserById(id: Long): Mono<User> {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(RuntimeException("Пользователь с id $id не найден")))
            .map { it }
    }

    override fun getAllUsers(): Flux<User> {
        return userRepository.findAll()
            .map { it }
    }
}
