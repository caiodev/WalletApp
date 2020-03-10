package br.com.caiodev.walletapp.utils.base.apiResponse

import com.squareup.moshi.Json

data class Error(
    @field:Json(name = "code") val code: Int = 0,
    @field:Json(name = "message") val message: String = ""
)