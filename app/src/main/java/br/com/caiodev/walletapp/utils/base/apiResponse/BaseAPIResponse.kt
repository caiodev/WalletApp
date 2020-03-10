package br.com.caiodev.walletapp.utils.base.apiResponse

import br.com.caiodev.walletapp.sections.login.model.UserAccount
import br.com.caiodev.walletapp.sections.userStatements.model.viewTypes.Statements
import com.squareup.moshi.Json

data class BaseAPIResponse(
    @field:Json(name = "userAccount") val data: UserAccount,
    @field:Json(name = "statementList") val statementList: MutableList<Statements>,
    @field:Json(name = "error") val error: Error
)