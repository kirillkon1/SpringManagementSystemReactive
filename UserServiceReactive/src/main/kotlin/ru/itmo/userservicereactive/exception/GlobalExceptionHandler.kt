package ru.itmo.userservicereactive.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
        val errors =
            ex.bindingResult.allErrors.associate { error ->
                val fieldName = (error as org.springframework.validation.FieldError).field
                val errorMessage = error.defaultMessage
                fieldName to errorMessage
            }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<Map<String, String>> {
        val error =
            mapOf(
                "error" to ex.message!!,
                "time" to LocalDateTime.now().toString(),
            )
        ex.printStackTrace()
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<Map<String, String>> {
        val error = mapOf("error" to "Доступ запрещен")
        return ResponseEntity(error, HttpStatus.FORBIDDEN)
    }
}
