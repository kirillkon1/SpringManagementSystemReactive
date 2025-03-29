@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.analyticsservice.controller

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import ru.itmo.analyticsservice.model.TaskEvent
import ru.itmo.analyticsservice.service.TaskEventService

@RestController
@RequestMapping("/api/analytics")
class AnalyticsController(
    private val taskEventService: TaskEventService,
) {
    @GetMapping("/tasks")
    fun getAllTasks(): Flux<TaskEvent> {
        return taskEventService.getAllTaskEvents()
    }
}
