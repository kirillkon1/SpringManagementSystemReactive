package ru.itmo.userservicereactive.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import ru.itmo.userservicereactive.model.User

@Repository
interface UserRepository : ReactiveCrudRepository<User, Long> {
    fun findByUsername(username: String): Mono<User>

    fun existsByUsername(username: String): Mono<Boolean>

    fun existsByEmail(email: String): Mono<Boolean>
}
