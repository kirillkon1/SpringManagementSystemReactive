@file:Suppress("ktlint:standard:filename")

package ru.itmo.taskservicereactive

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
import ru.itmo.taskmanagementservice.dto.TaskDTO
import ru.itmo.taskservicereactive.model.Task
import ru.itmo.taskservicereactive.model.TaskPriority
import ru.itmo.taskservicereactive.model.TaskStatus
import ru.itmo.taskservicereactive.repository.TaskRepository
import ru.itmo.taskservicereactive.service.impl.TaskServiceImpl

@ExtendWith(MockitoExtension::class)
class TaskServiceImplTest {
    @Mock
    private lateinit var taskRepository: TaskRepository

    @InjectMocks
    private lateinit var taskService: TaskServiceImpl

    private val sampleDto =
        TaskDTO(
            title = "Test Task",
            description = "Test Description",
            status = TaskStatus.NEW,
            priority = TaskPriority.NORMAL,
            assignedTo = 1L,
            projectId = 1L,
            dueDate = null,
        )

    private val sampleTask =
        Task(
            id = 1L,
            title = "Test Task",
            description = "Test Description",
            status = TaskStatus.NEW,
            priority = TaskPriority.NORMAL,
            assignedTo = 1L,
            projectId = 1L,
            dueDate = null,
            createdAt = null,
            updatedAt = null,
        )

    @Test
    fun `createTask should save and return task`() {
        doReturn(Mono.just(sampleTask)).`when`(taskRepository).save(any())

        StepVerifier.create(taskService.createTask(sampleDto))
            .expectNextMatches { it.title == sampleDto.title && it.description == sampleDto.description }
            .verifyComplete()

        verify(taskRepository, times(1)).save(any())
    }

    @Test
    fun `getTaskById should return task when found`() {
        doReturn(Mono.just(sampleTask)).`when`(taskRepository).findById(1L)

        StepVerifier.create(taskService.getTaskById(1L))
            .expectNext(sampleTask)
            .verifyComplete()

        verify(taskRepository, times(1)).findById(1L)
    }

    @Test
    fun `getTaskById should error when not found`() {
        doReturn(Mono.empty<Task>()).`when`(taskRepository).findById(2L)

        StepVerifier.create(taskService.getTaskById(2L))
            .expectError(RuntimeException::class.java)
            .verify()
    }

    @Test
    fun `updateTask should modify and save existing task`() {
        val updatedDto = sampleDto.copy(title = "Updated Task")
        val updatedTask = sampleTask.copy(title = "Updated Task")

        doReturn(Mono.just(sampleTask)).`when`(taskRepository).findById(1L)
        doReturn(Mono.just(updatedTask)).`when`(taskRepository).save(any())

        StepVerifier.create(taskService.updateTask(1L, updatedDto))
            .expectNextMatches { it.title == "Updated Task" }
            .verifyComplete()

        verify(taskRepository, times(1)).findById(1L)
        verify(taskRepository, times(1)).save(any())
    }

    @Test
    fun `deleteTask should remove existing task`() {
        doReturn(Mono.just(sampleTask)).`when`(taskRepository).findById(1L)
        doReturn(Mono.empty<Void>()).`when`(taskRepository).delete(sampleTask)

        StepVerifier.create(taskService.deleteTask(1L))
            .verifyComplete()

        verify(taskRepository, times(1)).findById(1L)
        verify(taskRepository, times(1)).delete(sampleTask)
    }

    @Test
    fun `searchTasks should return tasks containing title`() {
        doReturn(Flux.just(sampleTask)).`when`(taskRepository).findByTitleContainingIgnoreCase("test")

        StepVerifier.create(taskService.searchTasks("test"))
            .expectNext(sampleTask)
            .verifyComplete()

        verify(taskRepository, times(1)).findByTitleContainingIgnoreCase("test")
    }

    @Test
    fun `getAllTasks should return all tasks`() {
        doReturn(Flux.just(sampleTask)).`when`(taskRepository).findAll()

        StepVerifier.create(taskService.getAllTasks())
            .expectNext(sampleTask)
            .verifyComplete()
    }

    @Test
    fun `getTasksByStatus should return tasks with given status`() {
        doReturn(Flux.just(sampleTask)).`when`(taskRepository).findByStatus(TaskStatus.NEW)

        StepVerifier.create(taskService.getTasksByStatus(TaskStatus.NEW))
            .expectNext(sampleTask)
            .verifyComplete()
    }

    @Test
    fun `getTasksByPriority should return tasks with given priority`() {
        doReturn(Flux.just(sampleTask)).`when`(taskRepository).findByPriority(TaskPriority.NORMAL)

        StepVerifier.create(taskService.getTasksByPriority(TaskPriority.NORMAL))
            .expectNext(sampleTask)
            .verifyComplete()
    }

    @Test
    fun `getTasksByAssignedUser should return tasks assigned to user`() {
        doReturn(Flux.just(sampleTask)).`when`(taskRepository).findByAssignedTo(1L)

        StepVerifier.create(taskService.getTasksByAssignedUser(1L))
            .expectNext(sampleTask)
            .verifyComplete()
    }

    @Test
    fun `getTasksByProject should return tasks for project`() {
        doReturn(Flux.just(sampleTask)).`when`(taskRepository).findByProjectId(1L)

        StepVerifier.create(taskService.getTasksByProject(1L))
            .expectNext(sampleTask)
            .verifyComplete()
    }
}
