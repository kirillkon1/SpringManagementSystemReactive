package ru.itmo.projectservicereactive

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import ru.itmo.projectservicereactive.dto.ProjectDTO
import ru.itmo.projectservicereactive.model.Project
import ru.itmo.projectservicereactive.repository.ProjectRepository
import ru.itmo.projectservicereactive.service.impl.ProjectServiceImpl

@ExtendWith(MockitoExtension::class)
class ProjectServiceImplTest {
    @Mock
    private lateinit var projectRepository: ProjectRepository

    @InjectMocks
    private lateinit var projectService: ProjectServiceImpl

    private val baseDto =
        ProjectDTO(
            name = "Project A",
            description = "Description A",
            location = "Location A",
            startDate = null,
            endDate = null,
            budget = 1000.0,
        )

    private val baseProject =
        Project(
            id = 1L,
            name = "Project A",
            description = "Description A",
            location = "Location A",
            startDate = null,
            endDate = null,
            budget = 1000.0,
            createdAt = null,
            updatedAt = null,
        )

    @Test
    fun `createProject should save and return project`() {
        doReturn(Mono.just(baseProject)).`when`(projectRepository).save(any())

        StepVerifier.create(projectService.createProject(baseDto))
            .expectNextMatches { it.name == baseDto.name && it.budget == baseDto.budget }
            .verifyComplete()

        verify(projectRepository, times(1)).save(any())
    }

    @Test
    fun `getProjectById should return project when found`() {
        doReturn(Mono.just(baseProject)).`when`(projectRepository).findById(1L)

        StepVerifier.create(projectService.getProjectById(1L))
            .expectNext(baseProject)
            .verifyComplete()

        verify(projectRepository, times(1)).findById(1L)
    }

    @Test
    fun `getProjectById should error when not found`() {
        doReturn(Mono.empty<Project>()).`when`(projectRepository).findById(2L)

        StepVerifier.create(projectService.getProjectById(2L))
            .expectErrorMessage("Проект с id 2 не найден")
            .verify()
    }

    @Test
    fun `updateProject should modify and save existing project`() {
        val updatedDto = baseDto.copy(name = "Updated Project")
        val updatedProject = baseProject.copy(name = "Updated Project")

        doReturn(Mono.just(baseProject)).`when`(projectRepository).findById(1L)
        doReturn(Mono.just(updatedProject)).`when`(projectRepository).save(any())

        StepVerifier.create(projectService.updateProject(1L, updatedDto))
            .expectNextMatches { it.name == "Updated Project" }
            .verifyComplete()

        verify(projectRepository, times(1)).findById(1L)
        verify(projectRepository, times(1)).save(any())
    }

    @Test
    fun `deleteProject should remove existing project`() {
        doReturn(Mono.just(baseProject)).`when`(projectRepository).findById(1L)
        doReturn(Mono.empty<Void>()).`when`(projectRepository).delete(baseProject)

        StepVerifier.create(projectService.deleteProject(1L))
            .verifyComplete()

        verify(projectRepository, times(1)).findById(1L)
        verify(projectRepository, times(1)).delete(baseProject)
    }

    @Test
    fun `searchProjectsByName should return matching projects`() {
        doReturn(Flux.just(baseProject)).`when`(projectRepository).findByNameContainingIgnoreCase("proj")

        StepVerifier.create(projectService.searchProjectsByName("proj"))
            .expectNext(baseProject)
            .verifyComplete()

        verify(projectRepository, times(1)).findByNameContainingIgnoreCase("proj")
    }

    @Test
    fun `searchProjectsByLocation should return matching projects`() {
        doReturn(Flux.just(baseProject)).`when`(projectRepository).findByLocationContainingIgnoreCase("loc")

        StepVerifier.create(projectService.searchProjectsByLocation("loc"))
            .expectNext(baseProject)
            .verifyComplete()

        verify(projectRepository, times(1)).findByLocationContainingIgnoreCase("loc")
    }

    @Test
    fun `getAllProjects should return all projects`() {
        doReturn(Flux.just(baseProject)).`when`(projectRepository).findAll()

        StepVerifier.create(projectService.getAllProjects())
            .expectNext(baseProject)
            .verifyComplete()

        verify(projectRepository, times(1)).findAll()
    }

    @Test
    fun `getProjectsByBudgetRange should return projects within budget`() {
        val lower = 500.0
        val upper = 1500.0
        val projectWithin = baseProject.copy(budget = 1000.0)
        val projectAbove = baseProject.copy(budget = 2000.0)

        doReturn(Flux.just(projectWithin, projectAbove)).`when`(projectRepository).findByBudgetGreaterThanEqual(lower)

        StepVerifier.create(projectService.getProjectsByBudgetRange(lower, upper))
            .expectNext(projectWithin)
            .verifyComplete()

        verify(projectRepository, times(1)).findByBudgetGreaterThanEqual(lower)
    }
}
