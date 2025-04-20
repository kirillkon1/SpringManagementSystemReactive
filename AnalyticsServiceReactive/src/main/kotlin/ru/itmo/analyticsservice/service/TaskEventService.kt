package ru.itmo.analyticsservice.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.analyticsservice.model.TaskEvent
import ru.itmo.analyticsservice.repository.TaskEventRepository
import java.time.LocalDateTime

@Service
class TaskEventService(private val taskEventRepository: TaskEventRepository) {
    /**
     * Возвращает все события задач
     */
    fun getAllTaskEvents(): Flux<TaskEvent> = taskEventRepository.findAll()

    /**
     * Возвращает событие по ID или ошибку, если не найдено
     */
    fun getTaskEventById(id: Long): Mono<TaskEvent> =
        taskEventRepository.findById(id)
            .switchIfEmpty(Mono.error(RuntimeException("TaskEvent with id $id not found")))

    /**
     * Создаёт новое событие, устанавливая временные метки
     */
    fun createTaskEvent(event: TaskEvent): Mono<TaskEvent> =
        taskEventRepository.save(
            event.copy(
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            ),
        )

    /**
     * Обновляет существующее событие по ID
     */
    fun updateTaskEvent(
        id: Long,
        event: TaskEvent,
    ): Mono<TaskEvent> =
        getTaskEventById(id)
            .flatMap { existing ->
                val updated =
                    event.copy(
                        id = id,
                        createdAt = existing.createdAt,
                        updatedAt = LocalDateTime.now(),
                    )
                taskEventRepository.save(updated)
            }

    /**
     * Удаляет событие по ID
     */
    fun deleteTaskEvent(id: Long): Mono<Void> =
        getTaskEventById(id)
            .flatMap { taskEventRepository.delete(it) }

    /**
     * Считает общее количество событий
     */
    fun countTaskEvents(): Mono<Long> = taskEventRepository.count()

    /**
     * Фильтрация по приоритету
     */
    fun getTaskEventsByPriority(priority: String): Flux<TaskEvent> = taskEventRepository.findAll().filter { it.priority == priority }

    /**
     * Фильтрация по статусу
     */
    fun getTaskEventsByStatus(status: String): Flux<TaskEvent> = taskEventRepository.findAll().filter { it.status == status }

    /**
     * Фильтрация по проекту
     */
    fun getTaskEventsByProject(projectId: Long): Flux<TaskEvent> = taskEventRepository.findAll().filter { it.projectId == projectId }

    /**
     * Фильтрация по исполнителю
     */
    fun getTaskEventsByAssignedUser(userId: Long): Flux<TaskEvent> = taskEventRepository.findAll().filter { it.assignedTo == userId }

    /**
     * События в заданном диапазоне дат
     */
    fun getEventsBetweenDates(
        start: LocalDateTime,
        end: LocalDateTime,
    ): Flux<TaskEvent> =
        taskEventRepository.findAll()
            .filter { it.dueDate.isAfter(start) && it.dueDate.isBefore(end) }
}
