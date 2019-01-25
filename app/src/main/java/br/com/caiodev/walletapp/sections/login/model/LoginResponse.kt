package br.com.caiodev.walletapp.sections.login.model

import com.squareup.moshi.Json

class LoginResponse {
    @field:Json(name = "userAccount")
    val userAccount = UserAccount()
    @field:Json(name = "error")
    val error = Error()
}