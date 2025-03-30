package ru.itmo.analyticsservice.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "tasks_analytics")
data class TaskEvent(
    @Id
    val id: Long? = null,
    val title: String,
    val description: String,
    val dueDate: LocalDateTime,
    @Column("assign_to")
    val assignedTo: Long,
    val projectId: Long,
    val priority: String,
    val status: String,
    @Column("created_at")
    var createdAt: LocalDateTime? = null,
    @Column("updated_at")
    var updatedAt: LocalDateTime? = null,
)
