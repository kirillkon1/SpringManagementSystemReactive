package ru.itmo.userservicereactive.dto

class VerifyTokenDto(
    var userName: String? = null,
    var token: String? = null,
    var verifyStatus: Boolean = false,
    var details: String? = null,
)
