package br.com.caiodev.walletapp.statements.model

import br.com.caiodev.walletapp.login.model.Error
import com.squareup.moshi.Json

class UserStatement {
    @field:Json(name = "error") val error: Error? = null
    @field:Json(name = "statementList") val statementList = mutableListOf<Statement>()
}