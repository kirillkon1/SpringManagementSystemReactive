package ru.itmo.taskservicereactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskServiceReactiveApplication

fun main(args: Array<String>) {
    runApplication<TaskServiceReactiveApplication>(*args)
}
