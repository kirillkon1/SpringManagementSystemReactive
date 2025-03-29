package ru.itmo.projectservicereactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProjectServiceReactiveApplication

fun main(args: Array<String>) {
    runApplication<ProjectServiceReactiveApplication>(*args)
}
