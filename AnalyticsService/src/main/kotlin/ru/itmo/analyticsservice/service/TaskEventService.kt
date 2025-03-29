package ru.itmo.analyticsservice.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import ru.itmo.analyticsservice.model.TaskEvent
import ru.itmo.analyticsservice.repository.TaskEventRepository

@Service
class TaskEventService(private val taskEventRepository: TaskEventRepository) {
    fun getAllTaskEvents(): Flux<TaskEvent> {
        return taskEventRepository.findAll()
    }
}
