package ru.itmo.taskservicereactive.service.impl

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.taskmanagementservice.dto.TaskDTO
import ru.itmo.taskservicereactive.model.Task
import ru.itmo.taskservicereactive.model.TaskPriority
import ru.itmo.taskservicereactive.model.TaskStatus
import ru.itmo.taskservicereactive.repository.TaskRepository
import ru.itmo.taskservicereactive.service.TaskService

@Service
class TaskServiceImpl(private val taskRepository: TaskRepository) : TaskService {
    override fun createTask(dto: TaskDTO): Mono<Task> {
        val task =
            Task(
                title = dto.title!!,
                description = dto.description!!,
                status = dto.status,
                priority = dto.priority,
                assignedTo = dto.assignedTo!!,
                projectId = dto.projectId!!,
                dueDate = dto.dueDate,
            )
        return taskRepository.save(task)
    }

    override fun getTaskById(id: Long): Mono<Task> {
        return taskRepository.findById(id)
            .switchIfEmpty(Mono.error(RuntimeException("Задача с id $id не найдена")))
    }

    override fun updateTask(
        id: Long,
        dto: TaskDTO,
    ): Mono<Task> {
        return getTaskById(id)
            .flatMap { existingTask ->
                val updatedTask =
                    existingTask.copy(
                        title = dto.title!!,
                        description = dto.description!!,
                        status = dto.status,
                        priority = dto.priority,
                        assignedTo = dto.assignedTo!!,
                        projectId = dto.projectId!!,
                        dueDate = dto.dueDate,
                    )
                taskRepository.save(updatedTask)
            }
    }

    override fun deleteTask(id: Long): Mono<Void> {
        return getTaskById(id)
            .flatMap { task -> taskRepository.delete(task) }
    }

    override fun searchTasks(title: String): Flux<Task> {
        return taskRepository.findByTitleContainingIgnoreCase(title)
    }

    override fun getAllTasks(): Flux<Task> {
        return taskRepository.findAll()
    }

    override fun getTasksByStatus(status: TaskStatus): Flux<Task> {
        return taskRepository.findByStatus(status)
    }

    override fun getTasksByPriority(priority: TaskPriority): Flux<Task> {
        return taskRepository.findByPriority(priority)
    }

    override fun getTasksByAssignedUser(userId: Long): Flux<Task> {
        return taskRepository.findByAssignedTo(userId)
    }

    override fun getTasksByProject(projectId: Long): Flux<Task> {
        return taskRepository.findByProjectId(projectId)
    }
}
