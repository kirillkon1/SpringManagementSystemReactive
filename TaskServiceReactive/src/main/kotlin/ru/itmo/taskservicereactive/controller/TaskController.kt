@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.taskservicereactive.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.taskmanagementservice.dto.TaskDTO
import ru.itmo.taskservicereactive.model.Task
import ru.itmo.taskservicereactive.model.TaskPriority
import ru.itmo.taskservicereactive.model.TaskStatus
import ru.itmo.taskservicereactive.service.TaskService

@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val taskService: TaskService,
) {
    @PostMapping
    fun createTask(
        @Valid @RequestBody dto: TaskDTO,
    ): Mono<ResponseEntity<Task>> {
        return taskService.createTask(dto)
            .map { task -> ResponseEntity.status(HttpStatus.CREATED).body(task) }
    }

    @GetMapping("/{id}")
    fun getTaskById(
        @PathVariable id: Long,
    ): Mono<ResponseEntity<Task>> {
        return taskService.getTaskById(id)
            .map { task -> ResponseEntity.ok(task) }
    }

    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @Valid @RequestBody dto: TaskDTO,
    ): Mono<ResponseEntity<Task>> {
        return taskService.updateTask(id, dto)
            .map { task -> ResponseEntity.ok(task) }
    }

    @DeleteMapping("/{id}")
    fun deleteTask(
        @PathVariable id: Long,
    ): Mono<ResponseEntity<Void>> {
        return taskService.deleteTask(id)
            .then(Mono.just(ResponseEntity.noContent().build()))
    }

    @GetMapping("/search")
    fun searchTasks(
        @RequestParam title: String,
    ): Flux<Task> {
        return taskService.searchTasks(title)
    }

    @GetMapping
    fun getAllTasks(): Flux<Task> {
        return taskService.getAllTasks()
    }

    @GetMapping("/status/{status}")
    fun getTasksByStatus(
        @PathVariable status: TaskStatus,
    ): Flux<Task> {
        return taskService.getTasksByStatus(status)
    }

    @GetMapping("/priority/{priority}")
    fun getTasksByPriority(
        @PathVariable priority: TaskPriority,
    ): Flux<Task> {
        return taskService.getTasksByPriority(priority)
    }

    @GetMapping("/assigned/{userId}")
    fun getTasksByAssignedUser(
        @PathVariable userId: Long,
    ): Flux<Task> {
        return taskService.getTasksByAssignedUser(userId)
    }

    @GetMapping("/project/{projectId}")
    fun getTasksByProject(
        @PathVariable projectId: Long,
    ): Flux<Task> {
        return taskService.getTasksByProject(projectId)
    }
}
