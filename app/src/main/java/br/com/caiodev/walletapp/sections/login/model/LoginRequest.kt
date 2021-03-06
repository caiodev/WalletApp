package br.com.caiodev.walletapp.sections.login.model

import com.squareup.moshi.Json

data class LoginRequest(
    @field:Json(name = "user") val user: String,
    @field:Json(name = "password") val password: String
)