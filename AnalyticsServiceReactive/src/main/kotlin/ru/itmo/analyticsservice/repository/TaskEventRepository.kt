package ru.itmo.analyticsservice.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import ru.itmo.analyticsservice.model.TaskEvent

@Repository
interface TaskEventRepository : ReactiveCrudRepository<TaskEvent, Long>
