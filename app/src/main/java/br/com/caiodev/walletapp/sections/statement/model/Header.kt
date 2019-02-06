package br.com.caiodev.walletapp.sections.statement.model

import br.com.caiodev.walletapp.utils.RecyclerViewViewTypes
import br.com.caiodev.walletapp.utils.ViewType

data class Header(val headerName: String) : ViewType {

    override fun getViewType(): Int {
        return RecyclerViewViewTypes.header
    }
}