package ru.itmo.taskservicereactive.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import ru.itmo.taskservicereactive.model.Task
import ru.itmo.taskservicereactive.model.TaskPriority
import ru.itmo.taskservicereactive.model.TaskStatus

@Repository
interface TaskRepository : ReactiveCrudRepository<Task, Long> {
    fun findByTitleContainingIgnoreCase(title: String): Flux<Task>

    fun findByStatus(status: TaskStatus): Flux<Task>

    fun findByPriority(priority: TaskPriority): Flux<Task>

    fun findByAssignedTo(userId: Long): Flux<Task>

    fun findByProjectId(projectId: Long): Flux<Task>
}
