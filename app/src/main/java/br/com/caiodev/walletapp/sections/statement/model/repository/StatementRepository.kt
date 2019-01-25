package br.com.caiodev.walletapp.sections.statement.model.repository

import br.com.caiodev.walletapp.utils.repository.BaseRepository

class StatementRepository : BaseRepository() {

    suspend fun getUserStatement(): Any {
        return callApi(call = { service.getUserDetailsAsync().await() })
    }
}