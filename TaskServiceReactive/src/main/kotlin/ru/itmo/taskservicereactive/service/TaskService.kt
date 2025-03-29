package ru.itmo.taskservicereactive.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.taskmanagementservice.dto.TaskDTO
import ru.itmo.taskservicereactive.model.Task
import ru.itmo.taskservicereactive.model.TaskPriority
import ru.itmo.taskservicereactive.model.TaskStatus

interface TaskService {
    /**
     * Создает новую задачу.
     *
     * @param dto DTO задача, которую нужно создать
     * @return созданная задача
     */
    fun createTask(dto: TaskDTO): Mono<Task>

    /**
     * Получает задачу по ее уникальному идентификатору.
     *
     * @param id уникальный идентификатор задачи
     * @return задача с указанным идентификатором
     */
    fun getTaskById(id: Long): Mono<Task>

    /**
     * Обновляет существующую задачу по ее уникальному идентификатору.
     *
     * @param id уникальный идентификатор задачи, которую нужно обновить
     * @param dto DTO задачи с обновленными данными
     * @return обновленная задача
     */
    fun updateTask(
        id: Long,
        dto: TaskDTO,
    ): Mono<Task>

    /**
     * Удаляет задачу по ее уникальному идентификатору.
     *
     * @param id уникальный идентификатор задачи, которую нужно удалить
     */
    fun deleteTask(id: Long): Mono<Void>

    /**
     * Ищет задачи по их названию.
     *
     * @param title название задач для поиска
     * @return список задач, соответствующих указанному названию
     */
    fun searchTasks(title: String): Flux<Task>

    /**
     * Получает все задачи.
     *
     * @return список всех задач
     */
    fun getAllTasks(): Flux<Task>

    /**
     * Получает задачи по их статусу.
     *
     * @param status статус задач для выборки
     * @return список задач с указанным статусом
     */
    fun getTasksByStatus(status: TaskStatus): Flux<Task>

    /**
     * Получает задачи по их приоритету.
     *
     * @param priority приоритет задач для выборки
     * @return список задач с указанным приоритетом
     */
    fun getTasksByPriority(priority: TaskPriority): Flux<Task>

    /**
     * Получает задачи, назначенные на конкретного пользователя.
     *
     * @param userId идентификатор пользователя, на которого назначены задачи
     * @return список задач, назначенных на указанного пользователя
     */
    fun getTasksByAssignedUser(userId: Long): Flux<Task>

    /**
     * Получает задачи, связанные с определенным проектом.
     *
     * @param projectId идентификатор проекта
     * @return список задач, связанных с указанным проектом
     */
    fun getTasksByProject(projectId: Long): Flux<Task>
}
