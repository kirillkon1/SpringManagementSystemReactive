package ru.itmo.usermanagementservice.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Имя пользователя не может быть пустым")
    val username: String,
    @field:NotBlank(message = "Пароль не может быть пустым")
    val password: String,
)

/*

{
    "username": "kirillkon1",
    "password": "123321"
}

 */
