package br.com.caiodev.walletapp.sections.login.model

import com.squareup.moshi.Json

class Error {
    @field:Json(name = "code")
    val code = 0
    @field:Json(name = "message")
    val message = ""
}