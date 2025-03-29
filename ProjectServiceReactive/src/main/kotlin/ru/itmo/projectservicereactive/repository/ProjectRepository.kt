package ru.itmo.projectservicereactive.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import ru.itmo.projectservicereactive.model.Project

@Repository
interface ProjectRepository : ReactiveCrudRepository<Project, Long> {
    fun findByNameContainingIgnoreCase(name: String): Flux<Project>

    fun findByLocationContainingIgnoreCase(location: String): Flux<Project>

    fun findByBudgetGreaterThanEqual(budget: Double): Flux<Project>

    fun findByBudgetLessThanEqual(budget: Double): Flux<Project>
}
