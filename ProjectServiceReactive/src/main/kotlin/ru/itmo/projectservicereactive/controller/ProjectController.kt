@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.projectservicereactive.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.projectservicereactive.dto.ProjectDTO
import ru.itmo.projectservicereactive.model.Project
import ru.itmo.projectservicereactive.service.ProjectService

@RestController
@RequestMapping("/api/projects")
class ProjectController(private val projectService: ProjectService) {
    @PostMapping
    fun createProject(
        @RequestBody dto: ProjectDTO,
    ): ResponseEntity<Mono<Project>> {
        val createdProject = projectService.createProject(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject)
    }

    @GetMapping("/{id}")
    fun getProjectById(
        @PathVariable id: Long,
    ): ResponseEntity<Mono<Project>> {
        val project = projectService.getProjectById(id)
        return ResponseEntity.ok(project)
    }

    @PutMapping("/{id}")
    fun updateProject(
        @PathVariable id: Long,
        @RequestBody dto: ProjectDTO,
    ): ResponseEntity<Mono<Project>> {
        val updatedProject = projectService.updateProject(id, dto)
        return ResponseEntity.ok(updatedProject)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProject(
        @PathVariable id: Long,
    ): ResponseEntity<Mono<Void>> {
        return ResponseEntity.ok(projectService.deleteProject(id))
    }

    @GetMapping("/search/name")
    fun searchProjectsByName(
        @RequestParam name: String,
    ): ResponseEntity<Flux<Project>> {
        val projects = projectService.searchProjectsByName(name)
        return ResponseEntity.ok(projects)
    }

    @GetMapping("/search/location")
    fun searchProjectsByLocation(
        @RequestParam location: String,
    ): ResponseEntity<Flux<Project>> {
        val projects = projectService.searchProjectsByLocation(location)
        return ResponseEntity.ok(projects)
    }

    @GetMapping
    fun getAllProjects(): ResponseEntity<Flux<Project>> {
        val projects = projectService.getAllProjects()
        return ResponseEntity.ok(projects)
    }

    @GetMapping("/budget")
    fun getProjectsByBudgetRange(
        @RequestParam min: Double,
        @RequestParam max: Double,
    ): ResponseEntity<Flux<Project>> {
        val projects = projectService.getProjectsByBudgetRange(min, max)
        return ResponseEntity.ok(projects)
    }
}
