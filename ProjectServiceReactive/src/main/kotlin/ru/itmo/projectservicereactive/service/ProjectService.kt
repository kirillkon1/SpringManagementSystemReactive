package ru.itmo.projectservicereactive.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.projectservicereactive.dto.ProjectDTO
import ru.itmo.projectservicereactive.model.Project

interface ProjectService {
    /**
     * Создаёт новый проект.
     *
     * @param dto DTO-объект проекта, который необходимо создать.
     * @return Mono с созданным объектом проекта.
     */
    fun createProject(dto: ProjectDTO): Mono<Project>

    /**
     * Получает проект по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор проекта.
     * @return Mono с объектом проекта с указанным идентификатором.
     */
    fun getProjectById(id: Long): Mono<Project>

    /**
     * Обновляет существующий проект по его идентификатору.
     *
     * @param id уникальный идентификатор проекта, который необходимо обновить.
     * @param dto DTO-объект проекта с обновлёнными данными.
     * @return Mono с обновлённым объектом проекта.
     */
    fun updateProject(
        id: Long,
        dto: ProjectDTO,
    ): Mono<Project>

    /**
     * Удаляет проект по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор проекта, который необходимо удалить.
     * @return Mono, сигнализирующий об окончании операции удаления.
     */
    fun deleteProject(id: Long): Mono<Void>

    /**
     * Ищет проекты по названию.
     *
     * @param name название проекта или его часть для поиска.
     * @return Flux с проектами, соответствующими заданному названию.
     */
    fun searchProjectsByName(name: String): Flux<Project>

    /**
     * Ищет проекты по местоположению.
     *
     * @param location местоположение проекта или его часть для поиска.
     * @return Flux с проектами, соответствующими заданному местоположению.
     */
    fun searchProjectsByLocation(location: String): Flux<Project>

    /**
     * Получает список всех проектов.
     *
     * @return Flux со всеми доступными проектами.
     */
    fun getAllProjects(): Flux<Project>

    /**
     * Получает проекты, попадающие в заданный диапазон бюджета.
     *
     * @param minBudget минимальный бюджет проекта.
     * @param maxBudget максимальный бюджет проекта.
     * @return список проектов, бюджет которых находится в указанном диапазоне.
     */
    fun getProjectsByBudgetRange(
        minBudget: Double,
        maxBudget: Double,
    ): Flux<Project>
}
