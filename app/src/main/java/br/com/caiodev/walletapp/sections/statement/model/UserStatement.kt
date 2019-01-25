package br.com.caiodev.walletapp.sections.statement.model

import br.com.caiodev.walletapp.sections.login.model.Error
import com.squareup.moshi.Json

class UserStatement {
    @field:Json(name = "error") val error = Error()
    @field:Json(name = "statementList") val statementList = mutableListOf<Statement>()
}