package ru.itmo.projectservicereactive.service.impl

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.projectservicereactive.dto.ProjectDTO
import ru.itmo.projectservicereactive.model.Project
import ru.itmo.projectservicereactive.repository.ProjectRepository
import ru.itmo.projectservicereactive.service.ProjectService
import java.time.LocalDateTime

@Service
class ProjectServiceImpl(private val projectRepository: ProjectRepository) : ProjectService {
    override fun createProject(dto: ProjectDTO): Mono<Project> {
        val project =
            Project(
                name = dto.name!!,
                description = dto.description,
                location = dto.location,
                startDate = dto.startDate,
                endDate = dto.endDate,
                budget = dto.budget,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        return projectRepository.save(project)
    }

    override fun getProjectById(id: Long): Mono<Project> {
        return projectRepository.findById(id)
            .switchIfEmpty(Mono.error(RuntimeException("Проект с id $id не найден")))
    }

    override fun updateProject(
        id: Long,
        dto: ProjectDTO,
    ): Mono<Project> {
        return getProjectById(id)
            .flatMap { existingProject ->
                val updatedProject =
                    existingProject.copy(
                        name = dto.name!!,
                        description = dto.description,
                        location = dto.location,
                        startDate = dto.startDate,
                        endDate = dto.endDate,
                        budget = dto.budget,
                        updatedAt = LocalDateTime.now(),
                    )
                projectRepository.save(updatedProject)
            }
    }

    override fun deleteProject(id: Long): Mono<Void> {
        return getProjectById(id)
            .flatMap { project -> projectRepository.delete(project) }
    }

    override fun searchProjectsByName(name: String): Flux<Project> {
        return projectRepository.findByNameContainingIgnoreCase(name)
    }

    override fun searchProjectsByLocation(location: String): Flux<Project> {
        return projectRepository.findByLocationContainingIgnoreCase(location)
    }

    override fun getAllProjects(): Flux<Project> {
        return projectRepository.findAll()
    }

    override fun getProjectsByBudgetRange(
        minBudget: Double,
        maxBudget: Double,
    ): Flux<Project> {
        return projectRepository.findByBudgetGreaterThanEqual(minBudget)
            .filter { it.budget != null && it.budget <= maxBudget }
    }
}
