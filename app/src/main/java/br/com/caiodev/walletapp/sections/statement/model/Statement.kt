package br.com.caiodev.walletapp.sections.statement.model

import com.squareup.moshi.Json

class Statement {
    @field:Json(name = "date") val date = ""
    @field:Json(name = "desc") val desc = ""
    @field:Json(name = "title") val title = ""
    @field:Json(name = "value") val value = 0.0
}