package ru.itmo.userservicereactive.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.userservicereactive.model.User

interface UserService {
    fun getUserById(id: Long): Mono<User>

    fun getAllUsers(): Flux<User>
}
