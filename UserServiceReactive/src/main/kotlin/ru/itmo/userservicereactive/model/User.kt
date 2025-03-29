package ru.itmo.userservicereactive.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("users")
class User(
    @Id
    val id: Long? = null,
    var username: String,
    var password: String,
    var email: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    @Column("created_at")
    var createdAt: LocalDateTime? = null,
    @Column("updated_at")
    var updatedAt: LocalDateTime? = null,
)
