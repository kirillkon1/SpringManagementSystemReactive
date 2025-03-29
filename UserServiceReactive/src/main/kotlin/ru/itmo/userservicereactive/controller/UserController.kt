package ru.itmo.userservicereactive.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.userservicereactive.model.User
import ru.itmo.userservicereactive.service.UserService

@RestController
@RequestMapping("api/users")
class UserController(private val userService: UserService) {
    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Long,
    ): Mono<ResponseEntity<User>> {
        return userService.getUserById(id)
            .map { ResponseEntity.ok(it) }
    }

    @GetMapping
    fun getAllUsers(): Mono<ResponseEntity<Flux<User>>> {
        val usersFlux = userService.getAllUsers()
        return Mono.just(ResponseEntity.ok(usersFlux))
    }
}
