package ru.itmo.usermanagementservice.dto

data class AuthResponse(
    val username: String,
    val token: String,
)
