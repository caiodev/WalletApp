package br.com.caiodev.walletapp.login.model

import com.squareup.moshi.Json

class UserAccount {
    @field:Json(name = "agency") val agency = ""
    @field:Json(name = "balance") val balance = 0.0
    @field:Json(name = "bankAccount") val bankAccount = ""
    @field:Json(name = "name") val name = ""
    @field:Json(name = "userId") val userId = 0
}