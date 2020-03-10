package br.com.caiodev.walletapp.sections.userStatements.model.repository

import br.com.caiodev.walletapp.sections.userStatements.model.callInterface.Statements
import br.com.caiodev.walletapp.utils.base.RemoteRepository
import br.com.caiodev.walletapp.utils.factory.RetrofitService

class StatementRepository : RemoteRepository() {

    private val retrofitService = RetrofitService()

    suspend fun getUserStatement(userId: Int) = callApi(call = {
        retrofitService.getRetrofitService<Statements>().getUserDetailsAsync(userId).await()
    })
}