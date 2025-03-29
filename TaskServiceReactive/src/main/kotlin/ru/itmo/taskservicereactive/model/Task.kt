package ru.itmo.taskservicereactive.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "tasks")
data class Task(
    @Id
    val id: Long? = null,
    var title: String,
    var description: String? = null,
    var status: TaskStatus = TaskStatus.NEW,
    var priority: TaskPriority = TaskPriority.NORMAL,
    @Column("assign_to")
    var assignedTo: Long,
    var projectId: Long,
    var dueDate: LocalDateTime? = null,
    @Column("created_at")
    var createdAt: LocalDateTime? = null,
    @Column("updated_at")
    var updatedAt: LocalDateTime? = null,
)

enum class TaskStatus {
    NEW,
    IN_PROGRESS,
    IN_TESTING,
    DONE,
}

enum class TaskPriority {
    LOW,
    NORMAL,
    HIGH,
    CRITICAL,
}
