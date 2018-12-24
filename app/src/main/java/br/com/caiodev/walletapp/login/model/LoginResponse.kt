package br.com.caiodev.walletapp.login.model

import com.squareup.moshi.Json

class LoginResponse {

    @field:Json(name = "userAccount") val userAccount: UserAccount? = null
    @field:Json(name = "error") val error: Error? = null
}