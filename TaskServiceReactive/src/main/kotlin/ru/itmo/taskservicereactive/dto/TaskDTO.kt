@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.taskmanagementservice.dto

import jakarta.validation.constraints.*
import ru.itmo.taskservicereactive.model.TaskPriority
import ru.itmo.taskservicereactive.model.TaskStatus
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link ru.itmo.taskmanagementservice.model.Task}
 */
data class TaskDTO(
    @field:NotBlank(message = "Название задачи не может быть пустым")
    @field:NotNull
    val title: String? = null,
    @field:Size(message = "Описание не должно превышать 1000 символов", max = 1000)
    @field:NotNull
    val description: String? = null,
    @field:NotNull(message = "Статус задачи не может быть пустым")
    val status: TaskStatus = TaskStatus.NEW,
    @field:NotNull(message = "Приоритет задачи не может быть пустым")
    val priority: TaskPriority = TaskPriority.NORMAL,
    @field:Min(message = "ID пользователя должен быть положительным числом", value = 1)
    @field:NotNull
    val assignedTo: Long? = null,
    @field:Min(message = "ID проекта должен быть положительным числом", value = 1)
    @field:NotNull
    val projectId: Long? = null,
//    @field:FutureOrPresent(message = "Дедлайн должен быть в настоящем или будущем")
    val dueDate: LocalDateTime? = null,
) : Serializable

/*

{
    "title": "Новая задача",
    "description": "Описание задачи",
    "status": "NEW",
    "priority": "NORMAL",
    "assignedTo": 1,
    "projectId": 1,
    "dueDate": "2025-02-01T12:00:00"
 }

 */
