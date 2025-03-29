@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.userservicereactive.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val jwtSecretBase64: String,
    @Value("\${jwt.expirationMs}") private val jwtExpirationMs: Long,
) {
    private val key: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretBase64))

    /**
     * Генерирует JWT-токен (синхронно), но возвращает результат в Mono,
     * чтобы сервисы могли работать в реактивном стиле.
     */
    fun generateToken(username: String): Mono<String> {
        return Mono.fromSupplier {
            val now = Date()
            val expiryDate = Date(now.time + jwtExpirationMs)

            Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
        }
    }

    /**
     * Возвращает username из токена, обёрнутый в Mono
     */
    fun getUsernameFromToken(token: String): Mono<String> {
        return Mono.fromCallable {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
                .subject
        }
    }

    /**
     * Валидирует токен. При успехе вернёт Mono.just(true),
     * при ошибке — выбросит исключение (Mono.error).
     */
    fun validateToken(authToken: String): Mono<Boolean> {
        return Mono.fromCallable {
            try {
                Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken)
                true
            } catch (e: JwtException) {
                throw e
            }
        }
    }
}
