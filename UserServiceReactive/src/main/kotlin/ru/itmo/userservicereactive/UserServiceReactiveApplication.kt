package ru.itmo.userservicereactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserServiceReactiveApplication

fun main(args: Array<String>) {
    runApplication<UserServiceReactiveApplication>(*args)
}
