@file:Suppress("ktlint:standard:filename")

package ru.itmo.projectservicereactive.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table
data class Project(
    @Id
    val id: Long? = null,
    val name: String,
    val description: String? = null,
    val location: String? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val budget: Double? = null,
    @Column("created_at")
    var createdAt: LocalDateTime? = null,
    @Column("updated_at")
    var updatedAt: LocalDateTime? = null,
)
