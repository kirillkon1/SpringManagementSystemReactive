package ru.itmo.usermanagementservice.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "Имя пользователя не может быть пустым")
    val username: String,
    @field:NotBlank(message = "Пароль не может быть пустым")
    @field:Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    val password: String,
    @field:Email(message = "Электронная почта должна быть корректной")
    @field:NotBlank(message = "Электронная почта не может быть пустой")
    val email: String,
    @field:NotBlank(message = "Имя не может быть пустым")
    val firstName: String,
    @field:NotBlank(message = "Фамилия не может быть пустой")
    val lastName: String,
)

/*

{
    "username": "kirillkon1",
    "password": "123321",
    "email": "test@test.ru",
    "firstName": "kirill",
    "lastName": "kon"
}



 */
