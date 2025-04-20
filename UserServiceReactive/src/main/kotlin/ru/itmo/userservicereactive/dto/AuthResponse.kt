package ru.itmo.userservicereactive.dto

data class AuthResponse(
    val username: String,
    val token: String,
)
