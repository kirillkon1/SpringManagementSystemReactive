@file:Suppress("ktlint:standard:filename")

package ru.itmo.analyticsservice.service

import org.junit.jupiter.api.BeforeEach
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
import ru.itmo.analyticsservice.model.TaskEvent
import ru.itmo.analyticsservice.repository.TaskEventRepository
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class TaskEventServiceTest {
    @Mock
    private lateinit var repository: TaskEventRepository

    @InjectMocks
    private lateinit var service: TaskEventService

    private lateinit var sampleEvent: TaskEvent

    @BeforeEach
    fun setUp() {
        val now = LocalDateTime.of(2025, 4, 20, 12, 0)
        sampleEvent =
            TaskEvent(
                id = 1L,
                title = "Title",
                description = "Description",
                dueDate = now,
                assignedTo = 10L,
                projectId = 100L,
                priority = "HIGH",
                status = "NEW",
                createdAt = now,
                updatedAt = now,
            )
    }

    @Test
    fun `getAllTaskEvents should return all events`() {
        doReturn(Flux.just(sampleEvent)).`when`(repository).findAll()

        StepVerifier.create(service.getAllTaskEvents())
            .expectNext(sampleEvent)
            .verifyComplete()

        verify(repository, times(1)).findAll()
    }

    @Test
    fun `getTaskEventById should return event when found`() {
        doReturn(Mono.just(sampleEvent)).`when`(repository).findById(1L)

        StepVerifier.create(service.getTaskEventById(1L))
            .expectNext(sampleEvent)
            .verifyComplete()

        verify(repository, times(1)).findById(1L)
    }

    @Test
    fun `getTaskEventById should error when not found`() {
        doReturn(Mono.empty<TaskEvent>()).`when`(repository).findById(2L)

        StepVerifier.create(service.getTaskEventById(2L))
            .expectErrorMessage("TaskEvent with id 2 not found")
            .verify()

        verify(repository, times(1)).findById(2L)
    }

    @Test
    fun `createTaskEvent should set timestamps and save event`() {
        doReturn(Mono.just(sampleEvent)).`when`(repository).save(any())

        StepVerifier.create(service.createTaskEvent(sampleEvent))
            .expectNext(sampleEvent)
            .verifyComplete()

        verify(repository, times(1)).save(any())
    }

    @Test
    fun `updateTaskEvent should find and save updated event`() {
        val updatedEvent = sampleEvent.copy(title = "Updated")
        doReturn(Mono.just(sampleEvent)).`when`(repository).findById(1L)
        doReturn(Mono.just(updatedEvent)).`when`(repository).save(any())

        StepVerifier.create(service.updateTaskEvent(1L, updatedEvent))
            .expectNextMatches { it.title == "Updated" }
            .verifyComplete()

        verify(repository, times(1)).findById(1L)
        verify(repository, times(1)).save(any())
    }

    @Test
    fun `deleteTaskEvent should find and delete event`() {
        doReturn(Mono.just(sampleEvent)).`when`(repository).findById(1L)
        doReturn(Mono.empty<Void>()).`when`(repository).delete(sampleEvent)

        StepVerifier.create(service.deleteTaskEvent(1L))
            .verifyComplete()

        verify(repository, times(1)).findById(1L)
        verify(repository, times(1)).delete(sampleEvent)
    }

    @Test
    fun `countTaskEvents should return count from repository`() {
        doReturn(Mono.just(5L)).`when`(repository).count()

        StepVerifier.create(service.countTaskEvents())
            .expectNext(5L)
            .verifyComplete()

        verify(repository, times(1)).count()
    }

    @Test
    fun `getTaskEventsByPriority should filter by priority`() {
        val other = sampleEvent.copy(priority = "LOW")
        doReturn(Flux.just(sampleEvent, other)).`when`(repository).findAll()

        StepVerifier.create(service.getTaskEventsByPriority("HIGH"))
            .expectNext(sampleEvent)
            .verifyComplete()

        verify(repository, times(1)).findAll()
    }

    @Test
    fun `getTaskEventsByStatus should filter by status`() {
        val other = sampleEvent.copy(status = "DONE")
        doReturn(Flux.just(sampleEvent, other)).`when`(repository).findAll()

        StepVerifier.create(service.getTaskEventsByStatus("NEW"))
            .expectNext(sampleEvent)
            .verifyComplete()

        verify(repository, times(1)).findAll()
    }

    @Test
    fun `getTaskEventsByProject should filter by projectId`() {
        val other = sampleEvent.copy(projectId = 200L)
        doReturn(Flux.just(sampleEvent, other)).`when`(repository).findAll()

        StepVerifier.create(service.getTaskEventsByProject(100L))
            .expectNext(sampleEvent)
            .verifyComplete()

        verify(repository, times(1)).findAll()
    }

    @Test
    fun `getTaskEventsByAssignedUser should filter by assignedTo`() {
        val other = sampleEvent.copy(assignedTo = 20L)
        doReturn(Flux.just(sampleEvent, other)).`when`(repository).findAll()

        StepVerifier.create(service.getTaskEventsByAssignedUser(10L))
            .expectNext(sampleEvent)
            .verifyComplete()

        verify(repository, times(1)).findAll()
    }

    @Test
    fun `getEventsBetweenDates should filter by dueDate range`() {
        val before = sampleEvent.copy(dueDate = LocalDateTime.of(2025, 1, 1, 0, 0))
        val after = sampleEvent.copy(dueDate = LocalDateTime.of(2025, 12, 31, 23, 59))
        doReturn(Flux.just(before, sampleEvent, after)).`when`(repository).findAll()

        val start = LocalDateTime.of(2025, 4, 1, 0, 0)
        val end = LocalDateTime.of(2025, 5, 1, 0, 0)

        StepVerifier.create(service.getEventsBetweenDates(start, end))
            .expectNext(sampleEvent)
            .verifyComplete()

        verify(repository, times(1)).findAll()
    }
}
