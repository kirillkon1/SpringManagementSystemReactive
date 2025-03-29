package ru.itmo.usermanagementservice.dto

import ru.itmo.userservicereactive.model.User
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link ru.itmo.usermanagementservice.model.User}
 */
data class UserDTO(
    val id: Long,
    val username: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) : Serializable

fun mapToDto(user: User): UserDTO =
    UserDTO(
        id = user.id!!,
        username = user.username,
        email = user.username,
        firstName = user.firstName,
        lastName = user.lastName,
        createdAt = user.createdAt,
        updatedAt = user.updatedAt,
    )
