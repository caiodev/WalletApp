package br.com.caiodev.walletapp.repository

import br.com.caiodev.walletapp.utils.BaseRepository

class StatementRepository : BaseRepository() {

    suspend fun getUserStatement(): Any {
        return callApi(call = { service.getUserDetailsAsync().await() })
    }
}